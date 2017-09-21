package com.github.yurinevenchenov1970.quotes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.github.yurinevenchenov1970.quotes.adapter.QuoteAdapter;
import com.github.yurinevenchenov1970.quotes.adapter.QuoteClickListener;
import com.github.yurinevenchenov1970.quotes.bean.Quote;
import com.github.yurinevenchenov1970.quotes.net.ApiClient;
import com.github.yurinevenchenov1970.quotes.net.QuoteService;
import com.github.yurinevenchenov1970.quotes.utils.Utils;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.github.yurinevenchenov1970.quotes.net.Categories.FAMOUS;
import static com.github.yurinevenchenov1970.quotes.net.Categories.MOVIES;

public class QuotesServerFragment extends BasicFragment implements QuoteClickListener {

    private OnQuoteServerClickListener mListener;
    private List<Quote> mQuoteList;
    private QuoteAdapter mAdapter;
    private QuoteService mService;
    private boolean mIsFirstTime;

    public static QuotesServerFragment newInstance() {
        return new QuotesServerFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnQuoteServerClickListener) {
            mListener = (OnQuoteServerClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnQuoteServerClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mService = ApiClient.getClient().create(QuoteService.class);
        mIsFirstTime = true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataFromServer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void onSwipeRefresh() {
        getDataFromServer();
    }

    @Override
    public void onQuoteClick(int position) {
        if (mListener != null) {
            mListener.onQuoteServerClick(mQuoteList.get(position));
        }
    }

    private void getDataFromServer() {
        String category = Utils.readIsFamousChecked(getActivity()) ? FAMOUS : MOVIES;
        int count = Utils.readQuotesCount(getActivity());
        if (count == 1) {
            getOneQuote(category);
        } else {
            getQuotes(category, count);
        }
    }

    private void getOneQuote(String category) {
        Call<Quote> call = mService.getQuote(category, 1);
        call.enqueue(new Callback<Quote>() {
            @Override
            public void onResponse(Call<Quote> call, Response<Quote> response) {
                Quote quote = response.body();
                if (quote != null) {
                    mQuoteList = Collections.singletonList(quote);
                    showData();
                }
            }

            @Override
            public void onFailure(Call<Quote> call, Throwable t) {
                handleFailure();
            }
        });
    }

    private void getQuotes(String category, int count) {
        Call<List<Quote>> listCall = mService.getQuotesList(category, count);
        listCall.enqueue(new Callback<List<Quote>>() {
            @Override
            public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {
                List<Quote> quoteList = response.body();
                if (quoteList != null && !quoteList.isEmpty()) {
                    mQuoteList = quoteList;
                    showData();
                }
            }

            @Override
            public void onFailure(Call<List<Quote>> call, Throwable t) {
                handleFailure();
            }
        });
    }

    private void handleFailure() {
        onStopRefreshing();
        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_LONG).show();
    }

    private void showData() {
        fillAdapter();
        if (mIsFirstTime) {
            mIsFirstTime = false;
        } else {
            onStopRefreshing();
        }
    }

    private void fillAdapter() {
        mAdapter = new QuoteAdapter(mQuoteList, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void onStopRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public interface OnQuoteServerClickListener {
        void onQuoteServerClick(Quote quote);
    }
}