package com.example.food_order_demo.common;

import com.example.food_order_demo.model.Builder.Request;
import com.example.food_order_demo.model.User;

public class Common {
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";
    public static User currentUser;
    public static Request currentRequest;

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    public static final int PICK_IMAGE_REQUEST = 71;

    public static String convertCodeToStatus(String code){
        if (code.equals("0"))
            return "Đã đặt hàng";
        else if (code.equals("1"))
            return "Đang vận chuyển";
        else
            return "Đã giao";
    }


}
