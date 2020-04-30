package com.food.android.app.models;

public class ReviewModel {

    private String id;
    private String UserName;
    private String UserRevDesc;


    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    private float rating;

    public ReviewModel(){

    }


    public ReviewModel(String id, String userName, String userRevDesc,float rating) {
        this.id = id;
        UserName = userName;
        UserRevDesc = userRevDesc;
        this.rating = rating;

    }

    public String getUserImage() {
        return id;
    }

    public void setUserImage(String id) {
        this.id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserRevDesc() {
        return UserRevDesc;
    }

    public void setUserRevDesc(String userRevDesc) {
        UserRevDesc = userRevDesc;
    }

}
