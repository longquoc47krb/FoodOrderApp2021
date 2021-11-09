package com.example.food_order_demo.model;

public class User {
    private String userName;
    private String phone;
    private String password;
    private String fullName;
    private String email;
    private String address;
    private String role;
    private Restaurant restaurant;

    public User(){

    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    //customer
    public User(String userName, String phone, String password, String fullname, String email, String address, String role) {
        this.userName = userName;
        this.phone = phone;
        this.password = password;
        fullName = fullname;
        this.email = email;
        this.address = address;
        this.role = role;
    }
    //admin
    public User(String userName, String phone, String password, String email, String address, String role, Restaurant restaurant) {
        this.userName = userName;
        this.phone = phone;
        this.password = password;
        this.email = email;
        this.address = address;
        this.role = role;
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullName;
    }

    public void setFullname(String fullname) {
        fullName = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
