package com.example.food_order_demo.model;

import java.io.Serializable;

public class Foods implements Serializable {
    private String foodID = "";
    private String foodName = "", foodImage = "", price = "", description = "";
    private String category = "";

    public Foods() {
    }

    public Foods(String foodID, String foodName, String foodImage, String price, String description, String category) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    public CartItem toCartItem(int soLuong)
    {
        int total = Integer.parseInt(price)*soLuong;
        return new CartItem(foodID,foodName,String.valueOf(total),soLuong);
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }
}


