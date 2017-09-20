package com.github.yurinevenchenov1970.quotes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.yurinevenchenov1970.quotes.Quote;
import com.github.yurinevenchenov1970.quotes.R;

import java.util.List;

/**
 * @author Yuri Nevenchenov on 9/20/2017.
 */

public class QuoteAdapter extends RecyclerView.Adapter<QuoteViewHolder> {

    private List<Quote> mQuoteList;
    private QuoteClickListener mClickListener;

    public QuoteAdapter(List<Quote> quoteList, QuoteClickListener listener) {
        mQuoteList = quoteList;
        mClickListener = listener;
    }

    @Override
    public QuoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_item, null);
        return new QuoteViewHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(QuoteViewHolder holder, int position) {
        Quote item = mQuoteList.get(position);
        holder.setAuthorTextViewValue(item.getAuthor());
        holder.setQuoteTextViewValue(item.getQuote());
    }

    @Override
    public int getItemCount() {
        return mQuoteList.size();
    }
}