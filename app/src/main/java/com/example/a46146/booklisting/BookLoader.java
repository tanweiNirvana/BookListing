package com.example.a46146.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by 46146 on 2017/3/27.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private String mURL;

    public BookLoader(Context context, String url) {
        super(context);
        this.mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (mURL == null) {
            return null;
        }
        List<Book> books = QueryUtils.fetchBookData(mURL);
        return books;
    }
}
