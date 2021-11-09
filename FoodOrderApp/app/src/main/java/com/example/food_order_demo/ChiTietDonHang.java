package com.example.food_order_demo;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ChiTietDonHang extends AppCompatActivity {

    SanPhamTrongDonHangAdapter adapter;
    List<SanPhamTrongDonHang> sanphamList;
    ListView listView;
    String IDDonHang = "Chua Co";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_chitietdonhang);

        listView = findViewById(R.id.listCTDH);
        Bundle b = getIntent().getExtras();
        if(b != null)
            IDDonHang = b.getString("IDDonHang");
        //getActionBar().setTitle(IDDonHang);
        setTitle(IDDonHang);

        sanphamList = new ArrayList<>();
        sanphamList.add(new SanPhamTrongDonHang("Gà quay",2,100000));
        sanphamList.add(new SanPhamTrongDonHang("Gà quay",2,100000));
        sanphamList.add(new SanPhamTrongDonHang("Gà quay",2,100000));
        sanphamList.add(new SanPhamTrongDonHang("Gà quay",2,100000));
        sanphamList.add(new SanPhamTrongDonHang("Gà quay",2,100000));
        adapter = new SanPhamTrongDonHangAdapter(this,R.layout.dong_chitietdonhang,sanphamList);
        listView.setAdapter(adapter);
    }
}
