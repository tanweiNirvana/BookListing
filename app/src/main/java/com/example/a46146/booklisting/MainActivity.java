package com.example.a46146.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private static final String BOOK_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static BookAdapter bookAdapter = null;
    private RecyclerView recyclerView;
    private static String url = null;
    private static int ID = 0;
    private static String beforeURL = "";
    private TextView mEmptyTV;
    private View loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        final EditText editText = (EditText) findViewById(R.id.search_et);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_book);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mEmptyTV = (TextView) findViewById(R.id.empty);
        mEmptyTV.setVisibility(View.GONE);
        ImageView search = (ImageView) findViewById(R.id.search_iv);
        loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    url = BOOK_URL + editText.getText().toString();
                    loadingIndicator.setVisibility(VISIBLE);
                    mEmptyTV.setVisibility(View.GONE);
                    if (url == beforeURL) {
                        LoaderManager loaderManager = getLoaderManager();
                        loaderManager.initLoader(ID, null, MainActivity.this);
                    } else {
                        beforeURL = url;
                        LoaderManager loaderManager = getLoaderManager();
                        loaderManager.initLoader(ID++, null, MainActivity.this);
                    }
                }
            });
        } else {
            mEmptyTV.setVisibility(VISIBLE);
            mEmptyTV.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Log.d("debug", url);
        return new BookLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        loadingIndicator.setVisibility(View.GONE);
        recyclerView.setAdapter(null);
        if (data != null && !data.isEmpty()) {
            bookAdapter = new BookAdapter(data);
            recyclerView.setAdapter(bookAdapter);
        } else {
            mEmptyTV.setVisibility(VISIBLE);
            mEmptyTV.setText(R.string.no_books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {

    }
}
