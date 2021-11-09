package com.example.food_order_demo.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private String restaurantID = "";
    private String restaurantName = "";
    private List<CartItem> cartItemList = new ArrayList<>();
    private int CartTotal = 0;

    public Cart() {
        restaurantID = "";
        restaurantName = "";
        cartItemList = new ArrayList<>();
        CartTotal = 0;
    }

    public Cart(String restaurantID, String restaurantName, List<CartItem> cartItemList, int cartTotal) {
        this.restaurantID = restaurantID;
        this.cartItemList = cartItemList;
        CartTotal = cartTotal;
        this.restaurantName = restaurantName;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public int getCartTotal() {
        return CartTotal;
    }

    public void setCartTotal(int cartTotal) {
        CartTotal = cartTotal;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
