package com.example.food_order_demo.model.Builder;

public class NewRequestBuilder implements IRequestBuilder {

    private String requestID="";
    private String name="";
    private String phone="";
    private String address="";
    private String total="";
    private String status="";
    private String time="";
    private String restaurantID="";
    private String customerID="";

    @Override
    public IRequestBuilder setRequestID(String requestID) {
        this.requestID = requestID;
        return this;
    }

    @Override
    public IRequestBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public IRequestBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    @Override
    public IRequestBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    @Override
    public IRequestBuilder setTotal(String total) {
        this.total = total;
        return this;
    }

    @Override
    public IRequestBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public IRequestBuilder setTime(String time) {
        this.time = time;
        return this;
    }

    @Override
    public IRequestBuilder setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
        return this;
    }

    @Override
    public IRequestBuilder setCustomerID(String customerID) {
        this.customerID = customerID;
        return this;
    }

    public Request build(){
        return new Request(requestID,name,phone,address,total,status,time,restaurantID,customerID);
    }
}
