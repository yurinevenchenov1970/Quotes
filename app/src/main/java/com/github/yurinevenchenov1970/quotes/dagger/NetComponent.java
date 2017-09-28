package com.github.yurinevenchenov1970.quotes.dagger;

import com.github.yurinevenchenov1970.quotes.MainActivity;
import com.github.yurinevenchenov1970.quotes.QuotesRealmFragment;
import com.github.yurinevenchenov1970.quotes.QuotesServerFragment;
import com.github.yurinevenchenov1970.quotes.widget.QuoteWidgetProvider;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Yuri Nevenchenov on 9/28/2017.
 */
@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {

    void inject(MainActivity activity);

    void inject(QuotesRealmFragment fragment);

    void inject(QuotesServerFragment fragment);

    void inject(QuoteWidgetProvider provider);
}