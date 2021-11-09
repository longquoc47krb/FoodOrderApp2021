package com.example.food_order_demo.model.Builder;

public interface IRequestBuilder {

    IRequestBuilder setRequestID(String requestID);
    IRequestBuilder setName(String name);
    IRequestBuilder setPhone(String phone);
    IRequestBuilder setAddress(String address);
    IRequestBuilder setTotal(String total);
    IRequestBuilder setStatus(String status);
    IRequestBuilder setTime(String time);
    IRequestBuilder setRestaurantID(String restaurantID);
    IRequestBuilder setCustomerID(String customerID);
    Request build();
}
