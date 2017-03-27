package com.example.a46146.booklisting;

/**
 * Book class
 * Created by 46146 on 2017/3/27.
 */

public class Book {
    private String mTitle;
    private String mAuthor;
    private String mPrice;

    public Book(String mTitle, String mAuthor, String mPrice) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mPrice = mPrice;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmPrice() {
        return mPrice;
    }

}
