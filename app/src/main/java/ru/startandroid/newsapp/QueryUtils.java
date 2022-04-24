package ru.startandroid.newsapp;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving news data from theguardian.com.
 */
public class QueryUtils {
    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query theguardian dataset and return list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {
        Log.v(LOG_TAG, "TEST: fetchNewsData");

//        // Create URL object
//        URL url = createUrl(requestUrl);
//
//        // Perform HTTP request to the URL and receive a JSON response back
//        String jsonResponse = null;
//        try {
//            jsonResponse = makeHttpRequest(url);
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
//        }

        // TODO: remove temp data
        String jsonResponse = "{\"response\":{\"status\":\"ok\",\"userTier\":\"developer\",\"total\":2341415,\"startIndex\":11,\"pageSize\":10,\"currentPage\":2,\"pages\":234142,\"orderBy\":\"newest\",\"results\":[{\"id\":\"sport/2022/apr/23/mclaren-are-well-off-the-f1-pace-but-must-focus-on-the-future-to-find-a-fix\",\"type\":\"article\",\"sectionId\":\"sport\",\"sectionName\":\"Sport\",\"webPublicationDate\":\"2022-04-23T11:13:26Z\",\"webTitle\":\"McLaren are off the F1 pace – can their plan deliver the speed they need? | Giles Richards\",\"webUrl\":\"https://www.theguardian.com/sport/2022/apr/23/mclaren-are-well-off-the-f1-pace-but-must-focus-on-the-future-to-find-a-fix\",\"apiUrl\":\"https://content.guardianapis.com/sport/2022/apr/23/mclaren-are-well-off-the-f1-pace-but-must-focus-on-the-future-to-find-a-fix\",\"isHosted\":false,\"pillarId\":\"pillar/sport\",\"pillarName\":\"Sport\"},{\"id\":\"sport/2022/apr/23/former-india-head-coach-gary-kirsten-keen-on-england-cricket-test-job\",\"type\":\"article\",\"sectionId\":\"sport\",\"sectionName\":\"Sport\",\"webPublicationDate\":\"2022-04-23T11:08:46Z\",\"webTitle\":\"Former India head coach Gary Kirsten keen on England Test job\",\"webUrl\":\"https://www.theguardian.com/sport/2022/apr/23/former-india-head-coach-gary-kirsten-keen-on-england-cricket-test-job\",\"apiUrl\":\"https://content.guardianapis.com/sport/2022/apr/23/former-india-head-coach-gary-kirsten-keen-on-england-cricket-test-job\",\"isHosted\":false,\"pillarId\":\"pillar/sport\",\"pillarName\":\"Sport\"},{\"id\":\"lifeandstyle/2022/apr/23/shobna-gulati-son-akshay-look-back\",\"type\":\"article\",\"sectionId\":\"lifeandstyle\",\"sectionName\":\"Life and style\",\"webPublicationDate\":\"2022-04-23T11:00:52Z\",\"webTitle\":\"‘I was shamed for having my son but I stood up for my choices’: Shobna Gulati and her son Akshay look back\",\"webUrl\":\"https://www.theguardian.com/lifeandstyle/2022/apr/23/shobna-gulati-son-akshay-look-back\",\"apiUrl\":\"https://content.guardianapis.com/lifeandstyle/2022/apr/23/shobna-gulati-son-akshay-look-back\",\"isHosted\":false,\"pillarId\":\"pillar/lifestyle\",\"pillarName\":\"Lifestyle\"},{\"id\":\"football/blog/2022/apr/23/arsenal-or-chelsea-where-will-the-wsl-trophy-end-up\",\"type\":\"article\",\"sectionId\":\"football\",\"sectionName\":\"Football\",\"webPublicationDate\":\"2022-04-23T11:00:52Z\",\"webTitle\":\"Arsenal or Chelsea? Where will the WSL trophy end up? | Louise Taylor\",\"webUrl\":\"https://www.theguardian.com/football/blog/2022/apr/23/arsenal-or-chelsea-where-will-the-wsl-trophy-end-up\",\"apiUrl\":\"https://content.guardianapis.com/football/blog/2022/apr/23/arsenal-or-chelsea-where-will-the-wsl-trophy-end-up\",\"isHosted\":false,\"pillarId\":\"pillar/sport\",\"pillarName\":\"Sport\"},{\"id\":\"sport/2022/apr/23/friendly-battle-for-the-no-10-shirt-has-englands-women-flying\",\"type\":\"article\",\"sectionId\":\"sport\",\"sectionName\":\"Sport\",\"webPublicationDate\":\"2022-04-23T11:00:51Z\",\"webTitle\":\"Friendly battle for the No 10 shirt has England’s women flying\",\"webUrl\":\"https://www.theguardian.com/sport/2022/apr/23/friendly-battle-for-the-no-10-shirt-has-englands-women-flying\",\"apiUrl\":\"https://content.guardianapis.com/sport/2022/apr/23/friendly-battle-for-the-no-10-shirt-has-englands-women-flying\",\"isHosted\":false,\"pillarId\":\"pillar/sport\",\"pillarName\":\"Sport\"},{\"id\":\"lifeandstyle/2022/apr/23/my-mother-was-a-ruthless-ditcher-of-friends-should-i-dump-my-most-annoying-mates\",\"type\":\"article\",\"sectionId\":\"lifeandstyle\",\"sectionName\":\"Life and style\",\"webPublicationDate\":\"2022-04-23T11:00:51Z\",\"webTitle\":\"My mother was a ruthless ditcher of friends – should I dump my most annoying mates?\",\"webUrl\":\"https://www.theguardian.com/lifeandstyle/2022/apr/23/my-mother-was-a-ruthless-ditcher-of-friends-should-i-dump-my-most-annoying-mates\",\"apiUrl\":\"https://content.guardianapis.com/lifeandstyle/2022/apr/23/my-mother-was-a-ruthless-ditcher-of-friends-should-i-dump-my-most-annoying-mates\",\"isHosted\":false,\"pillarId\":\"pillar/lifestyle\",\"pillarName\":\"Lifestyle\"},{\"id\":\"football/2022/apr/23/chelsea-west-ham-match-preview-premier-league\",\"type\":\"article\",\"sectionId\":\"football\",\"sectionName\":\"Football\",\"webPublicationDate\":\"2022-04-23T10:39:42Z\",\"webTitle\":\"Chelsea v West Ham: match preview\",\"webUrl\":\"https://www.theguardian.com/football/2022/apr/23/chelsea-west-ham-match-preview-premier-league\",\"apiUrl\":\"https://content.guardianapis.com/football/2022/apr/23/chelsea-west-ham-match-preview-premier-league\",\"isHosted\":false,\"pillarId\":\"pillar/sport\",\"pillarName\":\"Sport\"},{\"id\":\"us-news/2022/apr/23/texas-butterfly-center-targeted-by-far-right-conspiracy-theorists-to-reopen\",\"type\":\"article\",\"sectionId\":\"us-news\",\"sectionName\":\"US news\",\"webPublicationDate\":\"2022-04-23T10:00:51Z\",\"webTitle\":\"Texas butterfly center targeted by far-right conspiracy theorists to reopen\",\"webUrl\":\"https://www.theguardian.com/us-news/2022/apr/23/texas-butterfly-center-targeted-by-far-right-conspiracy-theorists-to-reopen\",\"apiUrl\":\"https://content.guardianapis.com/us-news/2022/apr/23/texas-butterfly-center-targeted-by-far-right-conspiracy-theorists-to-reopen\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"money/2022/apr/23/uk-train-tickets-the-great-rail-sale-is-on-but-is-there-a-catch\",\"type\":\"article\",\"sectionId\":\"money\",\"sectionName\":\"Money\",\"webPublicationDate\":\"2022-04-23T10:00:51Z\",\"webTitle\":\"UK train tickets: the great rail sale is on – but is there a catch?\",\"webUrl\":\"https://www.theguardian.com/money/2022/apr/23/uk-train-tickets-the-great-rail-sale-is-on-but-is-there-a-catch\",\"apiUrl\":\"https://content.guardianapis.com/money/2022/apr/23/uk-train-tickets-the-great-rail-sale-is-on-but-is-there-a-catch\",\"isHosted\":false,\"pillarId\":\"pillar/lifestyle\",\"pillarName\":\"Lifestyle\"},{\"id\":\"us-news/2022/apr/23/san-francisco-homelessness-street-team-fentanyl\",\"type\":\"article\",\"sectionId\":\"us-news\",\"sectionName\":\"US news\",\"webPublicationDate\":\"2022-04-23T10:00:50Z\",\"webTitle\":\"The daily battle to keep people alive as fentanyl ravages San Francisco’s Tenderloin\",\"webUrl\":\"https://www.theguardian.com/us-news/2022/apr/23/san-francisco-homelessness-street-team-fentanyl\",\"apiUrl\":\"https://content.guardianapis.com/us-news/2022/apr/23/san-francisco-homelessness-street-team-fentanyl\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"}]}}";

        // Extract relevant fields from the JSON response and create a list of {@link News}
        // Return list of {@link News}
        return extractItemsFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Return list of {@link News} object by parsing out information
     * about news from the input newsJSON string.
     */
    private static List<News> extractItemsFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<News> newsList = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of News objects with the corresponding data.
            JSONObject reader = new JSONObject(newsJSON);

            if (!reader.has("response")) return newsList;
            JSONObject response = reader.getJSONObject("response");

            if (!response.has("results")) return newsList;
            JSONArray items = response.getJSONArray("results");

            for (int i = 0; i < items.length(); i++) {
                JSONObject newsItem = items.getJSONObject(i);

                // Extract the value for the key called "webUrl"
                String url = newsItem.getString("webUrl");

                // Extract the value for the key called "webTitle"
                String title = newsItem.getString("webTitle");

                // Extract the value for the key called "sectionName"
                String sectionName = newsItem.getString("sectionName");

                // Extract the value for the key called "webPublicationDate"
                String publicationDate = newsItem.getString("webPublicationDate");

                News news = new News(url, title, sectionName, publicationDate);

                newsList.add(news);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }
        // Return the list of news
        return newsList;
    }
}
