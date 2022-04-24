package ru.startandroid.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Finding list for news items
        RecyclerView newsRecyclerView = findViewById(R.id.news_list);

        //TODO: change request URL
        List<News> newsList = QueryUtils.fetchNewsData("");

        // Creating new ArrayAdapter for news info
        NewsAdapter newsAdapter = new NewsAdapter(newsList);
        newsRecyclerView.setHasFixedSize(true);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Setting ArrayAdapter to the ListView
        newsRecyclerView.setAdapter(newsAdapter);

    }
}