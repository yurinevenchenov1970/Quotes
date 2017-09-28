package com.github.yurinevenchenov1970.quotes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.yurinevenchenov1970.quotes.adapter.QuoteAdapter;
import com.github.yurinevenchenov1970.quotes.adapter.QuoteClickListener;
import com.github.yurinevenchenov1970.quotes.bean.Quote;
import com.github.yurinevenchenov1970.quotes.dagger.QuoteApplication;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * @author Yuri Nevenchenov on 9/20/2017.
 */

public class QuotesRealmFragment extends BasicFragment implements QuoteClickListener {

    @Inject
    Realm mRealm;

    private OnQuoteRealmClickListener mListener;

    public static QuotesRealmFragment newInstance() {
        return new QuotesRealmFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnQuoteRealmClickListener) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((QuoteApplication) getActivity().getApplication())
                .getNetComponent()
                .inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillAdapter();
        onStopRefreshing();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void onSwipeRefresh() {
        fillAdapter();
        onStopRefreshing();
    }

    @Override
    public void onQuoteClick(View view, int position) {
        if (mListener != null) {
            mListener.onQuoteRealmClick(view, mQuoteList.get(position));
        }
    }

    private void onStopRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void fillAdapter() {
        mQuoteList = getQuoteList();
        mAdapter = new QuoteAdapter(mQuoteList, this);
        mRecyclerView.setAdapter((mAdapter));
    }

    private List<Quote> getQuoteList() {
        return mRealm.allObjects(Quote.class);
    }

    public interface OnQuoteRealmClickListener {
        void onQuoteRealmClick(View view, Quote quote);
    }
}