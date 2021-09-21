package com.hcmute.foodorder2021.models;

public class User {
    private String UserName;
    private String Phone;
    private String Password;
    private String Fullname;
    private String Email;
    private String Address;
    private String role;
    private String image;


    public User(String image) {
        this.image = image;
    }

    private Restaurant restaurant;
    public User(){

    }

    public User(String userName, String password) {
        UserName = userName;
        Password = password;
    }
    //shipper

    public User(String userName, String phone, String password, String role, Restaurant restaurant) {
        UserName = userName;
        Phone = phone;
        Password = password;
        this.role = role;
        this.restaurant = restaurant;
    }

    //customer
    public User(String userName, String phone, String password, String fullname, String email, String address, String role) {
        UserName = userName;
        Phone = phone;
        Password = password;
        Fullname = fullname;
        Email = email;
        Address = address;
        this.role = role;
    }
    //admin
    public User(String userName, String phone, String password, String email, String address, String role, Restaurant restaurant) {
        UserName = userName;
        Phone = phone;
        Password = password;
        Email = email;
        Address = address;
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
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}