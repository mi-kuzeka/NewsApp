package ru.startandroid.newsapp;

import android.net.Uri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class News {

    /**
     * The URL of the html content
     */
    private final String mWebUrl;

    /**
     * The title of the news item
     */
    private final String mTitle;

    /**
     * The name of the section
     */
    private final String mSectionName;

    /**
     * The combined date and time of publication
     * Format: 2022-04-23T11:18:18Z
     */
    private final String mPublicationDate;

    /**
     * Constructs a new {@link News} object
     *
     * @param webUrl          is the string with the URL of the html content
     * @param webTitle        is the title of the news item
     * @param sectionName     is the name of the section
     * @param publicationDate is the combined date and time of publication
     */
    public News(String webUrl, String webTitle, String sectionName, String publicationDate) {
        mWebUrl = webUrl;
        mTitle = webTitle;
        mSectionName = sectionName;
        mPublicationDate = publicationDate;
    }

    public Uri getWebUrl() {
        return Uri.parse(mWebUrl);
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public Date getDateTime() throws ParseException {
        SimpleDateFormat parser =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

        // Put the date in the parser, convert the string to Date class
        return parser.parse(mPublicationDate);
    }
}
