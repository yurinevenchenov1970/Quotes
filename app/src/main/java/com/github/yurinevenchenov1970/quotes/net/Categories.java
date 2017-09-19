package com.github.yurinevenchenov1970.quotes.net;

import android.support.annotation.StringDef;

/**
 * @author Yuri Nevenchenov on 9/19/2017.
 */

public class Categories {
    @StringDef({FAMOUS,
                MOVIES})

    public @interface Category {
    }

    public static final String FAMOUS = "famous";
    public static final String MOVIES = "movies";
}