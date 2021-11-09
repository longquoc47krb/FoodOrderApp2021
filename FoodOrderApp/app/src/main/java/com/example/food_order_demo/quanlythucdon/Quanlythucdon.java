package com.example.food_order_demo.quanlythucdon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.example.food_order_demo.R;

import java.util.ArrayList;

public class Quanlythucdon extends AppCompatActivity {
    Button btnThemmon;
    ListView listViewmonan;
    quanlymonAdapter adapter;
    ArrayList<quanlymon> quanlymons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_quanlythucdon);
        AnhXa();
        adapter=new quanlymonAdapter(this,R.layout.custom_listview,quanlymons);
        listViewmonan.setAdapter(adapter);
    }
    private void AnhXa(){
        listViewmonan=findViewById(R.id.listviewquanlymonan);
        quanlymons= new ArrayList<>();
        quanlymons.add(new quanlymon("Cơm Chiên",R.drawable.comchien));
        quanlymons.add(new quanlymon("Cơm Chiên",R.drawable.comchien));
        quanlymons.add(new quanlymon("Cơm Chiên",R.drawable.comchien));
        quanlymons.add(new quanlymon("Cơm Chiên",R.drawable.comchien));
        quanlymons.add(new quanlymon("Cơm Chiên",R.drawable.comchien));
        quanlymons.add(new quanlymon("Cơm Chiên",R.drawable.comchien));
        quanlymons.add(new quanlymon("Cơm Chiên",R.drawable.comchien));
    }
}