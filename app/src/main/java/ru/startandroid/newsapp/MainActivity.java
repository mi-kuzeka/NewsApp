package ru.startandroid.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>> {

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;
    private final String API_KEY = "fa5384f8-4d43-4cab-9188-18a5a7465203";
    private final String THE_GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";

    // The text the user input into the SearchView
    private String mUserRequest;
    // The page in the user request
    private int currentPage;

    // The list with news items
    private RecyclerView mNewsRecyclerView;

    // The array adapter for news items
    private NewsAdapter mNewsAdapter;

    // The AsyncTaskLoader for user request
    private NewsLoader mCurrentLoader;


    // The list of news for this request
    private List<News> mNewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Finding list for news items
        mNewsRecyclerView = findViewById(R.id.news_list);
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewsRecyclerView.setHasFixedSize(true);

        // TODO: change user request (set text from SearchView)
        mUserRequest = "";
        // TODO: set this value every time the user changed search request
        currentPage = 1;
        mNewsList = new ArrayList<>();

        // Creating new ArrayAdapter for news info
        initNewsAdapter();

        initLoader();
    }

    /**
     * Creating new ArrayAdapter for news info
     */
    private void initNewsAdapter() {
        mNewsAdapter = new NewsAdapter(mNewsRecyclerView, mNewsList, this);
        // Setting ArrayAdapter to the ListView
        mNewsRecyclerView.setAdapter(mNewsAdapter);

        //set load more listener for the RecyclerView adapter
        mNewsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mNewsList == null || mNewsList.isEmpty()) {
                    mNewsAdapter.setLoaded();
                    return;
                }
                // TODO: check if this is necessary
                mNewsList.add(null);
                mNewsAdapter.notifyItemInserted(mNewsList.size() - 1);
                mNewsList.remove(mNewsList.size() - 1);
                mNewsAdapter.notifyItemRemoved(mNewsList.size());

                if (mNewsList != null && !mNewsList.isEmpty()) currentPage += 1;
                initLoader();
            }
        });
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        mCurrentLoader = new NewsLoader(this, combineRequestUrl());
        return mCurrentLoader;
    }

    public void initLoader() {
        // Clear previous news data
//        if (mNewsAdapter != null) mNewsAdapter.clear();

        // TODO: Checking if there's internet on this device


        // Get loading manager
        LoaderManager loaderManager = getSupportLoaderManager();

        // Check if Loader already exists
        if (mCurrentLoader != null) {
            // If there's Loader already then set new user request for the query
            mCurrentLoader.setNewUrl(combineRequestUrl());
            return;
        }
        // If there's no Loader then initialize new Loader
        loaderManager.initLoader(NEWS_LOADER_ID, null, MainActivity.this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> newsList) {
        mNewsList.addAll(newsList);

        // TODO: Checking if there's internet on this device


//        // Clear the adapter of previous news data
//        if (mNewsAdapter == null) {
//            initNewsAdapter();
//        } else {
//            mNewsAdapter.clear();
//        }

        if (mNewsList == null || mNewsList.isEmpty()) {
            //TODO: If there's no news items for this query show message about no results
        } else {
            // If there is a valid list of {@link News}, then add them to the adapter's
            // data set. This will trigger the ListView to update.
//            mNewsAdapter.addAll(mNewsList, 0);
            mNewsAdapter.notifyDataSetChanged();
            mNewsAdapter.setLoaded();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        // Clear previous news data
//        if (mNewsAdapter != null) mNewsAdapter.clear();
    }

    /**
     * Create a query for receiving data by user request
     */
    private String combineRequestUrl() {
        Uri baseUri = Uri.parse(THE_GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("page", String.valueOf(currentPage));
        if (!TextUtils.isEmpty(mUserRequest))
            uriBuilder.appendQueryParameter("q", mUserRequest);
        // TODO replace with user filters
        //uriBuilder.appendQueryParameter("from-date", "2022-04-25");
        //uriBuilder.appendQueryParameter("tag", "politics/politics");
        uriBuilder.appendQueryParameter("api-key", API_KEY);
        return uriBuilder.toString();
    }
}