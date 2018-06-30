package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String GUARDIAN_API = "https://content.guardianapis.com/search?api-key=b8510f05-195a-4440-8030-e5f0df499deb";
    private static final int NEWS_LOADER_ID = 1;
    private NewsAdapter adapter;
    private TextView emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.listView);

        // Create a new adapter that takes the list of earthquakes as input
        adapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(adapter);

        //setting the empty view
        emptyView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyView);

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

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        getLoaderManager().initLoader(NEWS_LOADER_ID, null, MainActivity.this);


    }


    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, GUARDIAN_API);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        //hide the loading indicator when the search is finished
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        // Set empty state text to display "No news found."
        emptyView.setText(R.string.no_news);
        //clear the adapter of previous data
        adapter.clear();
        //if there is a valid list of news , then add them to the adapter
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();

    }
}


