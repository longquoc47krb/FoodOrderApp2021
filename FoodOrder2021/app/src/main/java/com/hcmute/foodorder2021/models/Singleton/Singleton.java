package com.hcmute.foodorder2021.models.Singleton;


import com.google.firebase.database.DatabaseReference;
import com.hcmute.foodorder2021.models.Restaurant;
import com.hcmute.foodorder2021.models.User;

public class Singleton {
    private static final Singleton currentAccount = new Singleton();
    private String UserID;
    private User user;
    private Restaurant restaurant;
    private DatabaseReference restaurantRef;
    private DatabaseReference userRef;
//    private Cart currentCart = new Cart();

    private Singleton(){}

    public static Singleton getInstance(){
        return currentAccount;
    }

    public DatabaseReference getUserRef() {
        return userRef;
    }

    public void setUserRef(DatabaseReference userRef) {
        this.userRef = userRef;
    }

    public DatabaseReference getRestaurantRef() {
        return restaurantRef;
    }

    public void setRestaurantRef(DatabaseReference restaurantRef) {
        this.restaurantRef = restaurantRef;
    }

    public User getUser() {
        if(user == null)
            user = new User();
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        if(restaurant == null)
            restaurant = new Restaurant();
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }


    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        this.UserID = userID;
    }


//    public Cart getCurrentCart() {
//        return currentCart;
//    }
//
//    public void setCurrentCart(Cart currentCart) {
//        this.currentCart = currentCart;
//    }

}
