package com.restorani;

import java.io.Serializable;
import java.util.HashMap;

public class RestaurantModel implements Serializable {

    private String address;
    private String name;
    private HashMap<String, CommentModel> comments;
    private String image;
    private String phone;
    private String id;


    public RestaurantModel() {
    }

    public RestaurantModel(String address, String name, HashMap<String, CommentModel> comments, String image, String phone) {
        this.address = address;
        this.name = name;
        this.comments = comments;
        this.image = image;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, CommentModel> getComments() {
        return comments;
    }

    public void setComments(HashMap<String, CommentModel> comments) {
        this.comments = comments;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
