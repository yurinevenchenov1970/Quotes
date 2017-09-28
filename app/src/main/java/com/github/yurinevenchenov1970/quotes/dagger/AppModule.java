package com.github.yurinevenchenov1970.quotes.dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Yuri Nevenchenov on 9/28/2017.
 */
@Module
public class AppModule {

    private Application mApplication;
    public AppModule(Application application){
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication(){
        return mApplication;
    }
}