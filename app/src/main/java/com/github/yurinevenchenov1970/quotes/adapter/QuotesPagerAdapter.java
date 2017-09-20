package com.github.yurinevenchenov1970.quotes.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.github.yurinevenchenov1970.quotes.QuotesRealmFragment;
import com.github.yurinevenchenov1970.quotes.QuotesServerFragment;

/**
 * @author Yuri Nevenchenov on 9/20/2017.
 */

public class QuotesPagerAdapter extends FragmentStatePagerAdapter {

    public static final int COUNT = 2;

    public QuotesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = QuotesServerFragment.newInstance();
                break;
            case 1:
                fragment = QuotesRealmFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "quotes" : "favorites";
    }
}