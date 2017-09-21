package com.github.yurinevenchenov1970.quotes.net;

import com.github.yurinevenchenov1970.quotes.bean.Quote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * @author Yuri Nevenchenov on 9/19/2017.
 */

public interface QuoteService {

    @Headers("X-Mashape-Key: ttUbmOF3RImshnnF61GVRWimePq6p1AFKAbjsnvHlCahKC3hQd")
    @GET("/")
    Call<List<Quote>> getQuotesList(@Query("cat") @Categories.Category String cat,
                                    @Query("count") int count);

    @Headers("X-Mashape-Key: ttUbmOF3RImshnnF61GVRWimePq6p1AFKAbjsnvHlCahKC3hQd")
    @GET("/")
    Call<Quote> getQuote(@Query("cat") @Categories.Category String cat,
                         @Query("count") int count);
}