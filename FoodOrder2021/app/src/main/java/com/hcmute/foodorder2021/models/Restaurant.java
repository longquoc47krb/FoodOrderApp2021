package com.hcmute.foodorder2021.models;

public class Restaurant {
    String restaurantID;
    String image;
    String restaurantName;

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

    public Restaurant() {
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