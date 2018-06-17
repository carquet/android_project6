package com.example.android.newsapp;

public class News {
    /**tag */
    private String mSectionName;

    /** Time of publication*/
    private String mTime;

    /** Title */
    private String mWebTitle;

    /** Website URL of the news */
    private String mUrl;

    /**
     * Construct a new News object
     * @param section
     * @param time
     * @param webTitle
     * @param url
     *
     */
    public News(String section, String time, String webTitle, String url){
        mSectionName = section;
        mTime = time;
        mWebTitle = webTitle;
        mUrl = url;

    }

    public String getmSectionName() {
        return mSectionName;
    }

    public String getmTime() {
        return mTime;
    }

    public String getmWebTitle() {
        return mWebTitle;
    }

    public String getmUrl() {
        return mUrl;
    }
}
