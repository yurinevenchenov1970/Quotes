package com.github.yurinevenchenov1970.quotes;

import android.content.Context;

public class QuotesServerFragment extends BasicFragment {

    private OnQuoteServerClickListener mListener;

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void onSwipeRefresh() {
        // TODO: 9/20/2017 set adapter from list of objects from server
        onStopRefreshing();
    }

    private void onStopRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public interface OnQuoteServerClickListener {
        void onQuoteServerClick(int position);
    }
}