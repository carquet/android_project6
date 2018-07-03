package com.example.android.newsapp;

public class News {
    /**
     * Constant value that represents no name was provided for this article
     */
    private static final String  NO_NAME_PROVIDED = "";
    /**
     * tag
     */
    private String mSectionName;

    /**
     * Time of publication
     */
    private String mTime;

    /**
     * Title
     */
    private String mWebTitle;

    /**
     * Website URL of the news
     */
    private String mUrl;

    //name of the contributo
    private String mAuthor;

    /**
     * Construct a new News object with author name
     *
     * @param section
     * @param time
     * @param webTitle
     * @param url
     * @param author
     */
    public News(String section, String time, String webTitle, String url, String author) {
        mSectionName = section;
        mTime = time;
        mWebTitle = webTitle;
        mUrl = url;
        mAuthor = author;

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

    public String getmAuthor() {
        return mAuthor;
    }

    public boolean hasName(){
        return mAuthor != NO_NAME_PROVIDED;
    }
}
