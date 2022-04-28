package ru.startandroid.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<NewsResponse> {

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * Key for passing URL to the {@link NewsViewingActivity}
     */
    public static final String NEWS_ITEM_URL_KEY = "news_item_url";

    private static final String API_KEY = "fa5384f8-4d43-4cab-9188-18a5a7465203";
    private static final String THE_GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search";
    private final String AND_QUERY_OPERATOR = "%20AND%20";
    private final String OR_QUERY_OPERATOR = "%20OR%20";
    private final String NOT_QUERY_OPERATOR = "%20NOT%20";

    // The text the user input into the SearchView
    private String mUserRequest;
    // Page in response to a user request
    private int currentPage;
    // Number of pages in response to a user request
    private int totalPages;

    // The list with news items
    private RecyclerView mNewsRecyclerView;
    // View for searching data
    private SearchView mSearchView;
    // Widget that detects vertical swiping and displays a distinctive progress bar
    private SwipeRefreshLayout mSwipeRefreshLayout;

    // Array adapter for news items
    private NewsAdapter mNewsAdapter;

    // Stores the fragment object currently displayed to the user
    private Fragment mCurrentActiveFragment;
    // Fragment for an empty list of news. Displayed if there are no news for this query
    private Fragment mEmptyFragment;
    // The fragment is shown if there's no internet on this device
    private Fragment mNoInternetFragment;

    // The AsyncTaskLoader for user request
    private NewsLoader mCurrentLoader;

    // The list of news for this request
    private List<News> mNewsList;
    private NewsResponse mNewsResponse;
    // Shows whether or not there is internet on this device
    private boolean noInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Finding list for news items
        mNewsRecyclerView = findViewById(R.id.news_list);
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewsRecyclerView.setHasFixedSize(true);

        // Initialize fragments for no internet and no results states
        mNoInternetFragment = new NoInternetFragment();
        mEmptyFragment = new NoResultsFragment();

        // Setting focus to the SearchView
        mSearchView = findViewById(R.id.search_view);
        mSearchView.requestFocus();

        // Find SwipeRefreshLayout view
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        // The listener for refreshing list
        SwipeRefreshLayout.OnRefreshListener onRefreshListener =
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        clearAllLoadedData();
                        initLoader();
                    }
                };
        // Set the listener
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);

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

        mNewsList = new ArrayList<>();

        // Creating new ArrayAdapter for news info
        initNewsAdapter();

        mUserRequest = "";

        // Page number for first loading data
        currentPage = 1;

        // Checking if there's internet on this device
        noInternet = !isOnline(this);
        if (noInternet) {
            // Show message about no internet connection
            setFragmentInsteadOfList(mNoInternetFragment);
            stopSwipeToRefreshWidget();
            return;
        }

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
        // Checking if there's internet on this device
        noInternet = !isOnline(MainActivity.this);
        if (noInternet) {
            // Show message about no internet connection
            setFragmentInsteadOfList(mNoInternetFragment);
            stopSwipeToRefreshWidget();
            return;
        }

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

        // Checking if there's internet on this device
        noInternet = !isOnline(MainActivity.this);
        if (noInternet) {
            // Show message about no internet connection
            setFragmentInsteadOfList(mNoInternetFragment);
            stopSwipeToRefreshWidget();
            return;
        }

        if (!mUserRequest.equals("")) mSearchView.clearFocus();

        if (mNewsList == null || mNewsList.isEmpty()) {
            //If there's no news items for this query show message about no results
            setFragmentInsteadOfList(mEmptyFragment);
        } else {
            // Remove fragment if it's currently displayed
            removeFragment();
            // If there is a valid list of {@link News}, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            mNewsAdapter.notifyDataSetChanged();
            mNewsAdapter.setLoaded();
        }
        // Stop refreshing when data has been loaded
        stopSwipeToRefreshWidget();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<NewsResponse> loader) {
        // Clear previous news data
        clearAllLoadedData();
    }

    // Check if device has internet connection
    private static boolean isOnline(Context context) {
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Problem checking internet connection", e);
        }
        return false;
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
        if (mNewsList != null) mNewsList.clear();
        if (mNewsResponse != null) mNewsResponse.clear();
        if (mNewsAdapter != null) mNewsAdapter.clear();
    }

    /**
     * Show fragment instead of news list
     */
    private void setFragmentInsteadOfList(Fragment fragment) {
        if (mCurrentActiveFragment != null && mCurrentActiveFragment.equals(fragment))
            return;

        mCurrentActiveFragment = fragment;
        // Replace container by fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .commit();
    }

    /**
     * Remove unnecessary fragment
     */
    private void removeFragment() {
        if (mCurrentActiveFragment != null) {
            // Go back to the Main Activity
            getSupportFragmentManager().beginTransaction().remove(mCurrentActiveFragment).commit();
            // There's no active fragments
            mCurrentActiveFragment = null;
        }
    }

    // Stop refreshing
    private void stopSwipeToRefreshWidget() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onDestroy() {
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.destroyLoader(NEWS_LOADER_ID);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        removeFragment();
        super.onStop();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mUserRequest = mSearchView.getQuery().toString();
        if (!TextUtils.isEmpty(mUserRequest)) {
            mSearchView.setQuery(mSearchView.getQuery(), true);
        }
    }
}