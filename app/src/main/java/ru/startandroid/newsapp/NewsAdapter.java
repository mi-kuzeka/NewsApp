package ru.startandroid.newsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News> localNewsList;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView sectionTextView;
        private final TextView dateTextView;

        public ViewHolder(View view) {
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

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param newsList List<News> containing the data to populate views to be used
     *                 by RecyclerView.
     */
    public NewsAdapter(List<News> newsList) {
        localNewsList = newsList;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.news_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        News newsItem = localNewsList.get(position);

        viewHolder.getTitleTextView().setText(newsItem.getTitle());

        viewHolder.getSectionTextView().setText(newsItem.getSectionName());

        Date publicationDate;
        try {
            publicationDate = newsItem.getDateTime();
        } catch (ParseException exception) {
            publicationDate = new Date();
        }
        viewHolder.getDateTextView().setText(getPublicationDateText(publicationDate));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localNewsList.size();
    }

//    public NewsAdapter(@NonNull Context context, List<News> newsList) {
//        super(context, 0, newsList);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        // Check if an existing view is being reused, otherwise inflate the view.
//        View listItemView = convertView;
//        if (listItemView == null) {
//            listItemView = LayoutInflater.from(getContext()).inflate(
//                    R.layout.news_item, parent, false);
//        }
//
//        News newsItem = getItem(position);
//
//        TextView titleTextView =listItemView.findViewById(R.id.web_title);
//        titleTextView.setText(newsItem.getTitle());
//
//        TextView sectionTextView = listItemView.findViewById(R.id.section_name);
//        sectionTextView.setText(newsItem.getSectionName());
//
//        Date publicationDate;
//        try {
//            publicationDate = newsItem.getDateTime();
//        } catch (ParseException exception) {
//            publicationDate = new Date();
//        }
//        TextView dateTextView = listItemView.findViewById(R.id.publication_date);
//        dateTextView.setText(getPublicationDateText(publicationDate));
//
//        return listItemView;
//    }

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
}
