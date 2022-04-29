package ru.startandroid.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class NewsViewingActivity extends AppCompatActivity {

    private String newsItemUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_viewing);

        Bundle b = getIntent().getExtras();
        newsItemUrl = "";
        if (b != null)
            newsItemUrl = b.getString(MainActivity.NEWS_ITEM_URL_KEY);

        WebView webView = findViewById(R.id.news_web_view);
        webView.setFocusableInTouchMode(false);
        webView.setFocusable(false);
        webView.setClickable(false);

        if (!TextUtils.isEmpty(newsItemUrl)) {
            webView.loadUrl(newsItemUrl);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest url) {
                    if (url.getUrl().toString().equals(newsItemUrl))
                        view.loadUrl(url.getUrl().toString());
                    return true;
                }
            });
        }

        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    WebSettingsCompat.setForceDark(webView.getSettings(),
                            WebSettingsCompat.FORCE_DARK_ON);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    WebSettingsCompat.setForceDark(webView.getSettings(),
                            WebSettingsCompat.FORCE_DARK_OFF);
                    break;
            }
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