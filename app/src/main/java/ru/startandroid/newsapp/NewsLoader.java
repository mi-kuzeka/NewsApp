package ru.startandroid.newsapp;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<NewsResponse> {
    private String mUrl;


    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public NewsResponse loadInBackground() {
        if (TextUtils.isEmpty(mUrl)) return null;

        /** Create the list of news  from {@link QueryUtils} */
        return QueryUtils.fetchNewsData(mUrl);
    }

    // Sets the URL for new user request
    public void setNewUrl(String newUrl) {
        mUrl = newUrl;
        forceLoad();
    }
}
