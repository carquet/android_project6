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
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News>{

        /**
         * Constructs a new {@link NewsAdapter}.
         *
         * @param context of the app
         * @param news is the list of earthquakes, which is the data source of the adapter
         */
        public NewsAdapter(Context context, List<News> news) {
            super(context, 0, news);
        }

        /**
         * Returns a list item view that displays information about the earthquake at the given position
         * in the list of earthquakes.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Check if there is an existing list item view (called convertView) that we can reuse,
            // otherwise, if convertView is null, then inflate a new list item layout.
            View listItemView = convertView;
                if (listItemView == null) {
                    listItemView = LayoutInflater.from(getContext()).inflate(
                            R.layout.list_item, parent, false);
                }

            // Find the earthquake at the given position in the list of earthquakes
            News currentNews = getItem(position);

                //TITLE
            // Find the TextView with view ID title
            TextView titleView = (TextView) listItemView.findViewById(R.id.title);
            String title = currentNews.getmWebTitle();
            // Display the title of the current news in that TextView
            titleView.setText(title);

            //TIME
            TextView timeView = (TextView) listItemView.findViewById(R.id.time);
            String time = currentNews.getmTime();
            timeView.setText(time);


            //SECTION
            TextView sectionView = (TextView) listItemView.findViewById(R.id.section);
            String section = currentNews.getmSectionName();
            sectionView.setText(section);
            //SECTION COLOUR ACCORDING TO SECTION
            GradientDrawable sectionBubble = (GradientDrawable) sectionView.getBackground();
            int sectionColor = getSectionColor(currentNews.getmSectionName());
            sectionBubble.setColor(sectionColor);

            return listItemView;
        }

        private int getSectionColor(String sectionName){
            int sectionColorResourceId;
            String section = sectionName.toLowerCase();
            switch (section){
                case "business" :
                    sectionColorResourceId = R.color.news1;
                    break;
                case "environment":
                    sectionColorResourceId = R.color.news2;
                    break;
                case "money":
                    sectionColorResourceId = R.color.news3;
                    break;
                case "football":
                    sectionColorResourceId = R.color.news1;
                    break;
                case "society":
                    sectionColorResourceId = R.color.news2;
                    break;
                case "politics":
                    sectionColorResourceId = R.color.news3;
                    break;
                default:
                    sectionColorResourceId = R.color.newsdefault;
                    break;
            }
            return ContextCompat.getColor(getContext(), sectionColorResourceId);
        }
}
