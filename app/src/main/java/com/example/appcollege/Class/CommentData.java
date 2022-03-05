package com.example.appcollege.Class;

public class CommentData
{
    String userID, userName, date, time, commentContent, key, newsKey;

    public CommentData()
    {

    }

    public CommentData(String userID, String userName, String date, String time, String commentContent, String key, String newsKey)
    {
        this.userID = userID;
        this.userName = userName;
        this.date = date;
        this.time = time;
        this.commentContent = commentContent;
        this.key = key;
        this.newsKey = newsKey;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNewsKey() {
        return newsKey;
    }

    public void setNewsKey(String newsKey) {
        this.newsKey = newsKey;
    }
}
