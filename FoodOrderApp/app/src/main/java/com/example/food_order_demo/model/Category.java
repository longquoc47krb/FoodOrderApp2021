package com.example.food_order_demo.model;

public class Category {
    private String CategoryID;
    private String CategoryName;

    public Category() {
    }

    public Category(String categoryID, String categoryName) {
        CategoryID = categoryID;
        CategoryName = categoryName;
    }


    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public Category(String categoryName) {
        CategoryName = categoryName;
    }
    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
