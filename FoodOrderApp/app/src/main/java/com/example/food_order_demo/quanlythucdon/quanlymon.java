package com.example.food_order_demo.quanlythucdon;

public class quanlymon {
    private String Ten;
    private int Hinh;

    public quanlymon(String ten, int hinh) {

        Ten = ten;
        Hinh = hinh;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public int getHinh() {
        return Hinh;
    }

    public void setHinh(int hinh) {
        Hinh = hinh;
    }
}
