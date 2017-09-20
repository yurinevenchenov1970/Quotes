package com.github.yurinevenchenov1970.quotes.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Yuri Nevenchenov on 9/20/2017.
 */

public class Utils {

    public static final String QUOTES_COUNT = "quotes_count";
    public static final String IS_FAMOUS_CHECKED = "is_famous_cheched";

    private Utils() {
        throw new IllegalStateException("can't create object");
    }

    public static void writeQuotesCount(Activity activity, String quotesCount) {
        int count;
        if (quotesCount == null || quotesCount.equals("0")) {
            count = 1;
        } else {
            count = Integer.parseInt(quotesCount);
        }

        if (count < 0) {
            count = 1;
        } else if (count > 10) {
            count = 10;
        }

        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(QUOTES_COUNT, count);
        editor.apply();
    }

    public static int readQuotesCount(Activity activity) {
        int defaultValue = 10;
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        return preferences.getInt(QUOTES_COUNT, defaultValue);
    }

    public static void writeIsFamousChecked(Activity activity, boolean isChecked) {
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_FAMOUS_CHECKED, isChecked);
        editor.apply();
    }

    public static boolean readIsFamousChecked(Activity activity) {
        boolean defaultValue = true;
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        return preferences.getBoolean(IS_FAMOUS_CHECKED, defaultValue);
    }
}