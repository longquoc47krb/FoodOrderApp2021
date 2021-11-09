package com.example.food_order_demo.user_interface.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.food_order_demo.ChinhSuaThongTin;
import com.example.food_order_demo.DangNhap;
import com.example.food_order_demo.R;
import com.example.food_order_demo.model.Singleton.Singleton;
import com.example.food_order_demo.model.User;
import com.google.firebase.auth.FirebaseAuth;


public class CustomerProfileFragment extends Fragment {

    Button btnDXtemp, btnChinhSuaThongTin;
    EditText tenNguoiDung,tenDangNhap,soDienThoai,diaChi;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    User user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.customer_fragment_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnChinhSuaThongTin = view.findViewById(R.id.btnChinhSuaThongTin);
        btnDXtemp = view.findViewById(R.id.btnDangXuat);
        tenNguoiDung = view.findViewById(R.id.edtTenNguoiDung);
        tenDangNhap = view.findViewById(R.id.edtTenDangNhap);
        soDienThoai = view.findViewById(R.id.edtSoDienThoai);
        diaChi = view.findViewById(R.id.edtDiaChi);
        user = Singleton.getInstance().getUser();
        Log.i("USER",user.toString());

        tenNguoiDung.setText(user.getFullname());
        tenDangNhap.setText(user.getUserName());
        soDienThoai.setText(user.getPhone());
        diaChi.setText(user.getAddress());

        btnChinhSuaThongTin.setOnClickListener(v -> {
        startActivity(new Intent(getContext(), ChinhSuaThongTin.class));
        });

        btnDXtemp.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getContext(), DangNhap.class));
        });
    }
}
