package com.restorani;

import java.io.Serializable;

public class AddCommentModel implements Serializable {

    private String date;
    private int rating;
    private String text;
    private String user;
    private String userId;


    public AddCommentModel() {
    }

    public AddCommentModel(String date, int rating, String text, String user, String userId) {
        this.date = date;
        this.rating = rating;
        this.text = text;
        this.user = user;
        this.userId = userId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
