package com.example.android.newsapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    /**
     * The part of the time string from the Guardian service that we use to display only the date
     * ("2018-06-30T20:39:00Z").
     */
    private static final String DATE_SEPARATOR = "T";

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param news    is the list of news, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    /**
     * Returns a list item view that displays information about the piece of news at the given position
     * in the list of news.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Find the news in the current position of news
        News currentNews = getItem(position);

        /**TITLE*/
        // Find the TextView with view ID title
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        String title = currentNews.getmWebTitle();
        // Display the title of the current news in that TextView
        titleView.setText(title);

        /**DATE OF PUBLICATION*/
        TextView timeView = (TextView) listItemView.findViewById(R.id.date);
        // The original time string (i.e. "2018-06-30T20:39:00Z") contains
        // a separator T between date and time then store the date separately from the time in 2 Strings,
        // so only the date can be displayed and time if needed in the future
        String originalTime = currentNews.getmTime();
        String datePublication;
        // Check whether the currentNews string contains the " T " text
        if (originalTime.contains(DATE_SEPARATOR)) {
            // Split the string into different parts (as an array of Strings)
            // based on the " T " text. We expect an array of 2 Strings, where
            // the first String will be "2018-06-30" and the second String will be "20:39:00Z".
            String[] parts = originalTime.split(DATE_SEPARATOR);
            // date should be "2018-06-30 " without " T " --> "2018-06-30"
            datePublication = parts[0];
            timeView.setText(datePublication);
        }


        /**SECTION*/
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section);
        String section = currentNews.getmSectionName();
        sectionView.setText(section);
        //SECTION COLOUR ACCORDING TO SECTION
        GradientDrawable sectionBubble = (GradientDrawable) sectionView.getBackground();
        int sectionColor = getSectionColor(currentNews.getmSectionName());
        sectionBubble.setColor(sectionColor);

        /**NAME OF THE AUTHOR*/
        //Evaluate whether there is a name or not
        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        // Check if a name is provided for this news or not
        if (currentNews.hasName()) {
            // If a name is available, display the provided name
            authorView.setText(currentNews.getmAuthor());
            // Make sure the view is visible
            authorView.setVisibility(View.VISIBLE);
        } else {
            // Otherwise hide the ImageView (set visibility to GONE)
            authorView.setVisibility(View.GONE);
        }
        return listItemView;
    }

    private int getSectionColor(String sectionName) {
        int sectionColorResourceId;
        String section = sectionName.toLowerCase();
        switch (section) {
            case "business":
                sectionColorResourceId = R.color.news1;
                break;
            case "education":
                sectionColorResourceId = R.color.news2;
                break;
            case "football":
                sectionColorResourceId = R.color.news3;
                break;
            default:
                sectionColorResourceId = R.color.newsdefault;
                break;
        }
        return ContextCompat.getColor(getContext(), sectionColorResourceId);
    }
}
