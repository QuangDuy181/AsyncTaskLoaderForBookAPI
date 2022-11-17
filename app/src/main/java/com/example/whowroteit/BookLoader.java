package com.example.whowroteit;

import android.content.AsyncTaskLoader;
import android.content.Context;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NonNls;

public class BookLoader extends AsyncTaskLoader<String> {

    private String mQueryString;

    BookLoader(@NonNull Context context, String queryStirng){
        super(context);
        mQueryString = queryStirng;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        onForceLoad();
    }

    @Override
    public String loadInBackground() {
        return NetworkUtils.getBookInfo(mQueryString);
    }

}
