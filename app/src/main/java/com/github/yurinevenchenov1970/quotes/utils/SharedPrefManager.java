package com.github.yurinevenchenov1970.quotes.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Yuri Nevenchenov on 9/20/2017.
 */

public class SharedPrefManager {

    private static final String QUOTES_COUNT = "quotes_count";
    private static final int QUOTES_COUNT_DEFAULT_VALUE = 10;
    private static final String IS_FAMOUS_CHECKED = "is_famous_cheched";

    private SharedPreferences mSharedPreferences;

    public SharedPrefManager(SharedPreferences sharedPreferences){
        mSharedPreferences = sharedPreferences;
    }

    public boolean hasConnection(Context context) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        return connected;
    }

    public int generateRandomNumber(int min, int max) {
        return min + (int) (Math.random() * (max - min));
    }

    public void writeQuotesCount(String quotesCount) {
        int count;
        if (quotesCount == null || quotesCount.equals("0")) {
            count = 1;
        } else {
            count = Integer.parseInt(quotesCount);
        }

        if (count < 0) {
            count = 1;
        } else if (count > QUOTES_COUNT_DEFAULT_VALUE) {
            count = QUOTES_COUNT_DEFAULT_VALUE;
        }

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(QUOTES_COUNT, count);
        editor.apply();
    }

    public int readQuotesCount() {
        return mSharedPreferences.getInt(QUOTES_COUNT, QUOTES_COUNT_DEFAULT_VALUE);
    }

    public void writeIsFamousChecked(boolean isChecked) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(IS_FAMOUS_CHECKED, isChecked);
        editor.apply();
    }

    public boolean readIsFamousChecked() {
        boolean defaultValue = true;
        return mSharedPreferences.getBoolean(IS_FAMOUS_CHECKED, defaultValue);
    }
}