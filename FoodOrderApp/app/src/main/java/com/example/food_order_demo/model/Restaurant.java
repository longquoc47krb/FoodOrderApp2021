package com.example.food_order_demo.model;

import java.io.Serializable;
import java.util.List;

public class Restaurant implements Serializable {
    String restaurantID;
    String image;
    String restaurantName;
    public List<String> danhmucList;

    public Restaurant() {
    }

    public Restaurant(String restaurantID, String image, String restaurantName) {
        this.restaurantID = restaurantID;
        this.image = image;
        this.restaurantName = restaurantName;
    }


    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Restaurant(String image, String restaurantName) {
        this.image = image;
        this.restaurantName = restaurantName;
    }
}
