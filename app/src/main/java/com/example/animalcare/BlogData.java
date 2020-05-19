package com.example.animalcare;

import android.widget.ImageView;

import com.google.firebase.database.ServerValue;

public class BlogData {

    private String userName;
    private String blogDate;
    private String blogText;
    private String userID;
    private String postKey;
    private Object BLOGTimeStamp;
    private String imageURL;

    public BlogData() {
    }

    public BlogData(String userName, String blogDate, String blogText,String userID) {
        this.userName = userName;
        this.userID = userID;
        this.blogDate = blogDate;
        this.blogText = blogText;
        this.BLOGTimeStamp = ServerValue.TIMESTAMP;
    }

    public BlogData(String userName, String myBlogDate ,String blogText) {
        this.userName = userName;
        this.blogText = blogText;
        this.blogDate = myBlogDate;

    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserName() {
        return userName;
    }

    public String getBlogDate() {
        return blogDate;
    }

    public String getBlogText() {
        return blogText;
    }

    public Object getBLOGTimeStamp() {
        return BLOGTimeStamp;
    }

    public String getUserID() {
        return userID;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBlogDate(String blogDate) {
        this.blogDate = blogDate;
    }

    public void setBlogText(String blogText) {
        this.blogText = blogText;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setBLOGTimeStamp(Object BLOGTimeStamp) {
        this.BLOGTimeStamp = BLOGTimeStamp;
    }
}
