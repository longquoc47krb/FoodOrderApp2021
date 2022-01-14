package com.example.food_order_demo.common;

import com.example.food_order_demo.model.Builder.Request;
import com.example.food_order_demo.model.User;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class Common {
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";
    public static User currentUser;
    public static Request currentRequest;
    public static String printCurrency(double currencyAmount) {
        Locale locale;

        locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        return numberFormat.format(currencyAmount);

    }
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
