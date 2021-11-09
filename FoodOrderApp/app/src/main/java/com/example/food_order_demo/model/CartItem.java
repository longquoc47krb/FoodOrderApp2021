package com.example.food_order_demo.model;

import java.util.PrimitiveIterator;

public class CartItem {
    private String foodID;
    private String foodName, price;
    private int soLuong;

    public CartItem() {
    }

    public CartItem(String foodID, String foodName, String price, int soLuong) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.price = price;
        this.soLuong = soLuong;
    }


    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }



    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
