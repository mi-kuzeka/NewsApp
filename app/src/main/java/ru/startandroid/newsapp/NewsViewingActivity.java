package ru.startandroid.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class NewsViewingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_viewing);

        Bundle b = getIntent().getExtras();
        String newsItemUrl = "";
        if (b != null)
            newsItemUrl = b.getString(MainActivity.NEWS_ITEM_URL_KEY);

        WebView webView = findViewById(R.id.news_web_view);
        if (!TextUtils.isEmpty(newsItemUrl)) {
            webView.loadUrl(newsItemUrl);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest url) {
                    view.loadUrl(url.getUrl().toString());
                    return false;
                }
            });
        }

        ImageView closeView = findViewById(R.id.close_view);
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}