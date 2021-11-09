package com.example.food_order_demo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_order_demo.model.Singleton.Singleton;
import com.example.food_order_demo.model.User;
import com.google.firebase.database.DatabaseReference;

public class ChinhSuaThongTin extends AppCompatActivity {

    EditText tenNguoiDung, tenDangNhap, soDienThoai, diaChi;
    Button btnSua;
    User user = Singleton.getInstance().getUser();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_chinhsuathongtin);

        //mapping
        tenNguoiDung = findViewById(R.id.edtTenNguoiDung);
        tenDangNhap = findViewById(R.id.edtTenDangNhap);
        soDienThoai = findViewById(R.id.edtSoDienThoai);
        diaChi = findViewById(R.id.edtDiaChi);
        btnSua = findViewById(R.id.btnSua);
        //end mapping

        tenNguoiDung.setText(user.getFullname());
        tenDangNhap.setText(user.getUserName());
        soDienThoai.setText(user.getPhone());
        diaChi.setText(user.getAddress());

        checkEmpty();

        btnSua.setOnClickListener(v -> {
           update();
        });


    }

    private void update() {
        DatabaseReference userRef = Singleton.getInstance().getUserRef();

        String tenND = tenNguoiDung.getText().toString();
        String tenDN = tenDangNhap.getText().toString();
        String sdt = soDienThoai.getText().toString();
        String diachi = diaChi.getText().toString();

        userRef.child("userName").setValue(tenDN);
        userRef.child("fullName").setValue(tenND);
        userRef.child("address").setValue(diachi);
        userRef.child("phone").setValue(sdt);

        Singleton.getInstance().getUser().setFullname(tenND);
        Singleton.getInstance().getUser().setUserName(tenDN);
        Singleton.getInstance().getUser().setAddress(diachi);
        Singleton.getInstance().getUser().setPhone(sdt);

    }

    private void checkEmpty() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String tenND = tenNguoiDung.getText().toString();
                String tenDN = tenDangNhap.getText().toString();
                String sdt = soDienThoai.getText().toString();
                String diachi = diaChi.getText().toString();
                btnSua.setEnabled(!tenDN.isEmpty() && !tenND.isEmpty() && !sdt.isEmpty() && !diachi.isEmpty());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        tenNguoiDung.addTextChangedListener(textWatcher);
        tenDangNhap.addTextChangedListener(textWatcher);
        soDienThoai.addTextChangedListener(textWatcher);
        diaChi.addTextChangedListener(textWatcher);
    }
}
