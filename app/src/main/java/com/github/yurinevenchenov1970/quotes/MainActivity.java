package com.github.yurinevenchenov1970.quotes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.github.yurinevenchenov1970.quotes.adapter.QuotesPagerAdapter;
import com.github.yurinevenchenov1970.quotes.bean.Quote;
import com.github.yurinevenchenov1970.quotes.dagger.QuoteApplication;
import com.github.yurinevenchenov1970.quotes.utils.SharedPrefManager;

import javax.inject.Inject;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements
        QuotesServerFragment.OnQuoteServerClickListener,
        QuotesRealmFragment.OnQuoteRealmClickListener {

    @Inject
    Realm mRealm;

    @Inject
    SharedPrefManager mPrefManager;

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private QuotesPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((QuoteApplication) getApplication())
                .getNetComponent()
                .inject(this);
        mPagerAdapter = new QuotesPagerAdapter(getSupportFragmentManager());
        initUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            showSettingsDialog();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.exit_app)
                .setMessage(R.string.are_you_shure)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .show();
    }

    @Override
    public void onQuoteServerClick(View view, final Quote quote) {
        Snackbar.make(view, R.string.add_quote, Snackbar.LENGTH_LONG)
                .setAction(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addQuoteToFavorites(quote);
                    }
                })
                .show();
    }

    @Override
    public void onQuoteRealmClick(View view, final Quote quote) {
        Snackbar.make(view, R.string.remove_quote, Snackbar.LENGTH_LONG)
                .setAction(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeQuoteFromFavorites(quote);
                        showMessagePullToRefresh();
                    }
                })
                .show();
    }

    //region private methods

    private void initUI() {
        initToolbar();
        initViewPager();
        initTabLayout();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void initTabLayout() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void showSettingsDialog() {
        View view = getLayoutInflater().inflate(R.layout.setting_dialog, null);
        final TextInputEditText countEditText = (TextInputEditText) view.findViewById(R.id.category_count_edit_text);
        final RadioButton famousButton = (RadioButton) view.findViewById(R.id.famous_radio_button);
        final RadioButton moviesButton = (RadioButton) view.findViewById(R.id.movies_radio_button);

        countEditText.setText(String.valueOf(mPrefManager.readQuotesCount()));
        famousButton.setChecked(mPrefManager.readIsFamousChecked());
        moviesButton.setChecked(!famousButton.isChecked());

        new AlertDialog.Builder(this)
                .setTitle(R.string.settings)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveSettings(countEditText.getText().toString(), famousButton.isChecked());
                        showMessagePullToRefresh();
                    }
                })
                .setView(view)
                .show();
    }

    private void saveSettings(String count, boolean isFamousChecked) {
        mPrefManager.writeQuotesCount(count);
        mPrefManager.writeIsFamousChecked(isFamousChecked);
    }

    private void addQuoteToFavorites(Quote quote) {
        mRealm.beginTransaction();
        Quote newQuote = mRealm.createObject(Quote.class);
        newQuote.setAuthor(quote.getAuthor());
        newQuote.setQuote(quote.getQuote());
        mRealm.commitTransaction();
    }

    private void removeQuoteFromFavorites(final Quote quote) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Quote quoteToRemove = realm.where(Quote.class)
                        .equalTo("quote", quote.getQuote())
                        .findFirst();
                quoteToRemove.removeFromRealm();
            }
        });
    }

    private void showMessagePullToRefresh() {
        Toast.makeText(getApplicationContext(), R.string.pull_to_refresh, Toast.LENGTH_LONG)
                .show();
    }

    //endregion
}