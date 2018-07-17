package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    //"https://content.guardianapis.com/search?use-date=published&show-tags=contributor&q=education&api-key=b8510f05-195a-4440-8030-e5f0df499deb"
    private static final String GUARDIAN_API = "https://content.guardianapis.com/search";
    private static final int NEWS_LOADER_ID = 1;
    private NewsAdapter adapter;
    private TextView emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.listView);

        //setting the empty view
        emptyView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyView);

        // Create a new adapter that takes the list of news as input
        adapter = new NewsAdapter(this, new ArrayList<News>());

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

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);


        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyView.setText(R.string.no_internet_connection);
        }


    }


    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        //String orderBy = sharedPrefs.getString(
        String section = sharedPrefs.getString(getString(R.string.settings_section_key), getString(R.string.settings_section_default));

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_API);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //"https://content.guardianapis.com/search?use-date=published&show-tags=contributor&section=education&api-key=b8510f05-195a-4440-8030-e5f0df499deb"
        // Append query parameter and its value. For example, the `format=gjson`
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("section", section); //football, business, etc
        uriBuilder.appendQueryParameter("api-key", "b8510f05-195a-4440-8030-e5f0df499deb");

        // Return the completed uri `"https://content.guardianapis.com/search?show-tags=contributor&section={preference}&api-key=myapikey
        return new NewsLoader(this, uriBuilder.toString());
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

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //A. this method passes the menu item that is selected
    //B. An potions Menu may have more than one item: to determined which one we call getItemID
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


