package com.example.android.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String GUARDIAN_API = "https://content.guardianapis.com/search?api-key=b8510f05-195a-4440-8030-e5f0df499deb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        //Launch an async task in the background to fetch the information from the guardian API
        NewsAsyncTask newsTask = new NewsAsyncTask();
        newsTask.execute(GUARDIAN_API);


    }

    /**
     * Update the UI with the given earthquake information.
     */
    private void updateUi(List<News> news) {
        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.listView);

        // Create a new adapter that takes the list of earthquakes as input
        final NewsAdapter adapter = new NewsAdapter(this, news);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(adapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current news that was clicked on
                News currentNews = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getmUrl());
                // Create a new intent to view the news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                websiteIntent.setData(newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }

    //create a subclass to do the task in the background in Async
    private class NewsAsyncTask extends AsyncTask<String, Void, List<News>> {

        @Override
        protected List<News> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            //Get info from the API
            // Perform the HTTP request for news data and process the response.
            List<News> news = Utils.fetchNewsInfo(urls[0]);
            return news;

        }

        @Override
        protected void onPostExecute(List<News> news) {
            // Update the information displayed to the user if the information exists if not return early
            if (news == null) {
                return;
            }
            updateUi(news);
        }
    }
}
