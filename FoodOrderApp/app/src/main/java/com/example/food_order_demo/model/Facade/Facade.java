package com.example.food_order_demo.model.Facade;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.food_order_demo.model.Category;
import com.example.food_order_demo.model.Foods;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Facade {
    private final static Facade instance = new Facade();

    private Facade(){}

    public static Facade getInstance()
    {
        return instance;
    }

    public void xoaDanhMuc(String restaurantID, String danhmucXoa)
    {
        DatabaseReference quanAnRef = FirebaseDatabase.getInstance().getReference("Restaurants").child(restaurantID);
        quanAnRef.child("details").child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()) {
                    Category category = ds.getValue(Category.class);
                    category.setCategoryID(ds.getKey());
                    if(category.getCategoryName().equals(danhmucXoa))
                    {
                        quanAnRef.child("details").child("Category").child(category.getCategoryID()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                xoaMonAnTheoDanhMuc(restaurantID,danhmucXoa);
                            }
                        });
                        break;
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void themMonAn(String restaurantID, Foods foodToBeAdded)
    {
        DatabaseReference quanAnRef = FirebaseDatabase.getInstance().getReference("Restaurants").child(restaurantID);
        DatabaseReference food = quanAnRef.child("details").child("Food").child(foodToBeAdded.getFoodID());
        food.setValue(foodToBeAdded).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("themMonAn","OK");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });
        capNhatDanhMucQuanAn(restaurantID);
    }

    public void xoaMonAnTheoDanhMuc(String restaurantID, String danhmuc)
    {
        DatabaseReference quanAnRef = FirebaseDatabase.getInstance().getReference("Restaurants").child(restaurantID);
        DatabaseReference foods = quanAnRef.child("details").child("Food");
        foods.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()) {
                    Foods food = ds.getValue(Foods.class);
                    if(food.getCategory().equals(danhmuc)){
                        foods.child(ds.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("xoaMonAnTheoDanhMuc", "Success "+food.getFoodName());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void xoaMonAn(String restaurantID, String foodToBeDeleted){
        DatabaseReference quanAnRef = FirebaseDatabase.getInstance().getReference("Restaurants").child(restaurantID);
        DatabaseReference food = quanAnRef.child("details").child("Food").child(foodToBeDeleted);
        food.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
        capNhatDanhMucQuanAn(restaurantID);
    }

    public void capNhatDanhMucQuanAn(String restaurantID)
    {
        ArrayList<String> danhmucList = new ArrayList<>();
        DatabaseReference quanAnRef = FirebaseDatabase.getInstance().getReference("Restaurants").child(restaurantID);
        DatabaseReference h= quanAnRef.child("details").child("Food");
        quanAnRef.child("details").child("Category").removeValue();
        h.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                danhmucList.clear();
                for(DataSnapshot ds:snapshot.getChildren()) {
                    Foods food = ds.getValue(Foods.class);
                    if (!danhmucList.contains(food.getCategory()))
                    {
                        danhmucList.add(food.getCategory());
                    }

                }
                Set<String> set = new HashSet<>(danhmucList);
                danhmucList.clear();
                danhmucList.addAll(set);
                for(String danhmuc : danhmucList)
                {
                    Log.i("capNhatDanhMucQuanAn",danhmuc);
                    themDanhMuc(restaurantID, danhmuc);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

    }

    public void themDanhMuc(String restaurantID, String danhmuc) {
        DatabaseReference quanAnRef = FirebaseDatabase.getInstance().getReference("Restaurants").child(restaurantID);
        Category category = new Category(danhmuc);
        quanAnRef.child("details").child("Category").push().setValue(category);
    }


}
