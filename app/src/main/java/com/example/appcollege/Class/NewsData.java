package com.example.appcollege.Class;

public class NewsData
{
    String title, description, image, date, time, key;
    Integer likes, comments;

    public NewsData()
    {

    }

    public NewsData(String title, String description, String image, String date, String time, Integer likes, Integer comments, String key) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.date = date;
        this.time = time;
        this.likes = likes;
        this.comments = comments;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
