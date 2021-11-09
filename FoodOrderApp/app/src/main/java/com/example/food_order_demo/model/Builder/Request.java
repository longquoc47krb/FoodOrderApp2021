package com.example.food_order_demo.model.Builder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Request implements Serializable {
    private String requestID;
    private String name;
    private String phone;
    private String address;
    private String total;
    private String status;
    private String time;
    private String restaurantID;
    private String customerID;

    public Request() {
    }

    public Request(String requestID,String name, String phone, String address, String total, String status, String time, String restaurantID, String customerID) {
        this.requestID = requestID;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.total = total;
        this.status = status; // Mặc định là 0, 0:Đã đặt, 1:Đang vận chuyển, 2:Đã chuyển
        this.time = time;
        this.restaurantID = restaurantID;
        this.customerID = customerID;

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }
}
