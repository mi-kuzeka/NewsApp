package ru.startandroid.newsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private Activity activity;
    private List<News> newsList;
    private final int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param newsList List<News> containing the data to populate views to be used
     *                 by RecyclerView.
     */
    public NewsAdapter(RecyclerView recyclerView, List<News> newsList,
                       Activity activity) {
        this.newsList = newsList;
        this.activity = activity;

        final LinearLayoutManager linearLayoutManager =
                (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        isLoading = true;
                        onLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return newsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView sectionTextView;
        private final TextView dateTextView;

        public NewsViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            this.titleTextView = (TextView) view.findViewById(R.id.web_title);
            this.sectionTextView = (TextView) view.findViewById(R.id.section_name);
            this.dateTextView = (TextView) view.findViewById(R.id.publication_date);
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public TextView getSectionTextView() {
            return sectionTextView;
        }

        public TextView getDateTextView() {
            return dateTextView;
        }
    }

    // "Loading item" ViewHolder
    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.loading_spinner);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity)
                    .inflate(R.layout.news_item, parent, false);
            return new NewsViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity)
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
        // Create a new view, which defines the UI of the list item

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof NewsViewHolder) {
            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            News newsItem = this.newsList.get(position);
            NewsViewHolder newsViewHolder = (NewsViewHolder) viewHolder;
            Activity mainActivity = this.activity;

            newsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mainActivity, NewsViewingActivity.class);
                    Bundle b = new Bundle();
                    // Pass url to {@link NewsViewingActivity} for opening web page
                    b.putString(MainActivity.NEWS_ITEM_URL_KEY, newsItem.getWebUrl().toString());
                    intent.putExtras(b);
                    mainActivity.startActivity(intent);
                }
            });
            newsViewHolder.getTitleTextView().setText(newsItem.getTitle());
            newsViewHolder.getSectionTextView().setText(newsItem.getSectionName());

            Date publicationDate;
            try {
                publicationDate = newsItem.getDateTime();
            } catch (ParseException exception) {
                publicationDate = new Date();
            }
            newsViewHolder.getDateTextView().setText(getPublicationDateText(publicationDate));
        } else if (viewHolder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) viewHolder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.newsList == null ? 0 : this.newsList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    private String getPublicationDateText(Date date) {
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(date);
        String today = dateFormat.format(new Date());
        if (formattedDate.equals(today)) {
            SimpleDateFormat timeFormat =
                    new SimpleDateFormat("HH:mm", Locale.getDefault());
            return timeFormat.format(date);
        }
        return formattedDate;
    }

    /**
     * Clear all items in the adapter
     */
    public void clear() {
        if (this.newsList == null) return;
        int size = this.newsList.size();
        this.newsList.clear();
        notifyItemRangeRemoved(0, size);
    }
}
