package ru.startandroid.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
        implements LoaderManager.LoaderCallbacks<NewsResponse> {

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;
    private final String API_KEY = "fa5384f8-4d43-4cab-9188-18a5a7465203";
    private final String THE_GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";
    private final String AND_QUERY_OPERATOR = "%20AND%20";
    private final String OR_QUERY_OPERATOR = "%20OR%20";
    private final String NOT_QUERY_OPERATOR = "%20NOT%20";

    // The text the user input into the SearchView
    private String mUserRequest;
    // The page in the user request
    private int currentPage;
    // The count of pages for users query
    private int totalPages;

    // The list with news items
    private RecyclerView mNewsRecyclerView;
    // View for searching data
    private SearchView mSearchView;

    // The array adapter for news items
    private NewsAdapter mNewsAdapter;

    // The AsyncTaskLoader for user request
    private NewsLoader mCurrentLoader;

    // The list of news for this request
    private List<News> mNewsList;
    private NewsResponse mNewsResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Finding list for news items
        mNewsRecyclerView = findViewById(R.id.news_list);
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewsRecyclerView.setHasFixedSize(true);

        // Setting focus to the SearchView
        mSearchView = findViewById(R.id.search_view);
        mSearchView.requestFocus();

        // Set the listener for the SearchView
        mSearchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    /**
                     * This method starts loading when the user has submitted the request
                     * @param query is user request
                     */
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        mUserRequest = query.trim().replace(" ", AND_QUERY_OPERATOR);
                        clearAllLoadedData();
                        initLoader();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                }
        );
        // Show last news by default
        mUserRequest = "";
        // Page number for first loading data
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
                    // This is to avoid data duplication
                    mNewsAdapter.setLoaded();
                    return;
                }

                if (mNewsResponse != null &&
                        mNewsResponse.getCurrentPage() == mNewsResponse.getPagesCount()) {
                    mNewsAdapter.setLoaded();
                    return;
                }

                // Loading the next page
                currentPage += 1;
                initLoader();
            }
        });
    }

    @NonNull
    @Override
    public Loader<NewsResponse> onCreateLoader(int id, @Nullable Bundle args) {
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
    public void onLoadFinished(@NonNull Loader<NewsResponse> loader, NewsResponse newsResponse) {
        mNewsResponse = newsResponse;
        mNewsList.addAll(mNewsResponse.getNewsList());

        // TODO: Checking if there's internet on this device


//        // Clear the adapter of previous news data
//        if (mNewsAdapter == null) {
//            initNewsAdapter();
//        } else {
//            mNewsAdapter.clear();
//        }
        if (!mUserRequest.equals("")) mSearchView.clearFocus();

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
    public void onLoaderReset(@NonNull Loader<NewsResponse> loader) {
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

    private void clearAllLoadedData() {
        // Page number for first loading data by this query
        currentPage = 1;
        mNewsList.clear();
        mNewsResponse.clear();
        mNewsAdapter.clear();
    }
}