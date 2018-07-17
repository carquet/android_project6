package com.example.android.newsapp;

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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class Utils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = Utils.class.getSimpleName();

    //no need to make an instance of this class
    private Utils() {

    }

    public static List<News> fetchNewsInfo(String requestedUrl) {
        //1. create a Url object
        URL url = createUrl(requestedUrl);
        //2. Make an http request
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        //3.parse JSON
        // Extract relevant fields from the JSON response and create an {@link News} object
        return extractFromJson(jsonResponse);
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
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
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
     * Return an {@link News} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */
    private static List<News> extractFromJson(String jsonResponse) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<News> news = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONObject response = baseJsonResponse.getJSONObject("response");

            JSONArray resultsArray = response.getJSONArray("results");

            // If there are results in the features array
            if (resultsArray.length() > 0) {
                for (int index = 0; resultsArray.length() > index; index++) {
                    // Extract out the first feature (which is a news)
                    JSONObject firstNews = resultsArray.getJSONObject(index);

                    // Extract out section, date, title and weburl
                    String section = firstNews.getString("sectionName");
                    String date = firstNews.getString("webPublicationDate");
                    String title = firstNews.getString("webTitle");
                    String webUrl = firstNews.getString("webUrl");

                    //check for author's name
                    JSONArray tagArray = firstNews.getJSONArray("tags");
                    if (tagArray.length() > 0) {
                        for (int i = 0; tagArray.length() > i; i++) {
                            JSONObject contributor = tagArray.getJSONObject(i);
                            //extract the author's name
                            String authorName = contributor.getString("webTitle");
                            news.add(new News(section, date, title, webUrl, authorName));
                        }

                    } else {
                        // Create a new {@link Event} object
                        news.add(new News(section, date, title, webUrl, ""));
                    }

                }
                return news;
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return null;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
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
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }
}
