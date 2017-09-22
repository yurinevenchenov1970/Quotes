package com.github.yurinevenchenov1970.quotes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.github.yurinevenchenov1970.quotes.R;
import com.github.yurinevenchenov1970.quotes.bean.Quote;
import com.github.yurinevenchenov1970.quotes.net.ApiClient;
import com.github.yurinevenchenov1970.quotes.net.Categories;
import com.github.yurinevenchenov1970.quotes.net.QuoteService;
import com.github.yurinevenchenov1970.quotes.utils.Utils;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Yuri Nevenchenov on 9/22/2017.
 */

public class QuoteWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent intent = new Intent(context, QuoteWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quote_appwidget);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.get_button, pendingIntent);
        context.startService(intent);

        final AppWidgetManager manager = AppWidgetManager.getInstance(context);
        final ComponentName componentName = new ComponentName(context, QuoteWidgetProvider.class);
        manager.updateAppWidget(componentName, views);

        Realm realm = Realm.getInstance(context);
        RealmResults<Quote> quotes = realm.allObjects(Quote.class);
        if (Utils.hasConnection(context)) {
            QuoteService service = ApiClient.getClient().create(QuoteService.class);
            Call<Quote> call = service.getQuote(Categories.FAMOUS, 1);
            call.enqueue(new Callback<Quote>() {
                @Override
                public void onResponse(Call<Quote> call, Response<Quote> response) {
                    Quote quote = response.body();
                    if (quote != null) {
                        views.setTextViewText(R.id.quote_text_view, quote.getQuote());
                        views.setTextViewText(R.id.author_text_view, quote.getAuthor());
                        manager.updateAppWidget(componentName, views);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Quote> call, @NonNull Throwable t) {
                    views.setTextViewText(R.id.quote_text_view, "No connection (error)");
                }
            });
        } else if (quotes != null && !quotes.isEmpty()) {
            int randomIndex = Utils.generateRandomNumber(0, quotes.size());
            Quote quote = quotes.get(randomIndex);
            views.setTextViewText(R.id.quote_text_view, quote.getQuote());
            views.setTextViewText(R.id.author_text_view, quote.getAuthor());
            manager.updateAppWidget(componentName, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}