package com.example.food_order_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.food_order_demo.model.Singleton.Singleton;
import com.example.food_order_demo.model.User;
//import com.example.food_order_demo.quanlydanhmuc.CategoryActivity;
import com.example.food_order_demo.user_interface.CustomerMain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DangNhap extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    Button btnDangNhap, btnDangKy, btnQuenMatKhau;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    ConstraintLayout layout;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onStart() {
        super.onStart();
        if(!isDeviceProtectedStorage())
        {
            mAuth = FirebaseAuth.getInstance();
            if (mAuth.getCurrentUser() != null) {
                checkUserPermission();
            }
        }
        else Log.i("DangNhap","DeviceProtectedStorage");



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangnhap);

        mapping();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edtPassword.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Snackbar.make(layout, "Tên đăng nhập không được để trống", Snackbar.LENGTH_SHORT).setBackgroundTint(Color.RED).show();
                } else if (TextUtils.isEmpty(password)) {
                    Snackbar.make(layout, "Password không được để trống", Snackbar.LENGTH_SHORT).setBackgroundTint(Color.RED).show();
                } else if (password.length() < 6) {
                    Snackbar.make(layout, "Password không được nhỏ hơn 6", Snackbar.LENGTH_SHORT).setBackgroundTint(Color.RED).show();
                } else {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
                        signIn(email, password);
                        // checkUserAccessLevel();
                    } else {
                        Snackbar.make(layout, "Email không hợp lệ", Snackbar.LENGTH_SHORT).setBackgroundTint(Color.RED).show();
                        edtEmail.setError("Email không hợp lệ");

                    }
                }
            }
        });
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangNhap.this, DangKy.class));
            }
        });
        btnQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangNhap.this, ForgetPassword.class));
            }
        });
    }

    private void signIn(String email, String password) {

        progressDialog.setTitle(" Login");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    checkUserPermission();
                } else {
                    Snackbar.make(layout, "Sai tên đăng nhập hoặc mật khẩu", Snackbar.LENGTH_SHORT).setBackgroundTint(Color.RED).show();
                    progressDialog.dismiss();
                }
            }
        });


    }
    private void checkUserPermission(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference user = database.getReference("User").child(mAuth.getCurrentUser().getUid());
        Singleton.getInstance().setUserRef(user);
        Singleton.getInstance().setUserID(mAuth.getCurrentUser().getUid());
        Log.i("DangNhap","Đang đăng nhập");
        progressDialog.setTitle(" Login");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        ValueEventListener listener = new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Singleton.getInstance().setUser(user);
                Log.i("DangNhap","Đang đăng nhập " + user.getFullname());
                if(user.getRole().equals("admin")){
                    //Snackbar.make(layout,"Đăng nhập thành công: Người bán", BaseTransientBottomBar.LENGTH_LONG).setBackgroundTint(R.color.colorPrimary).show();
                    startActivity(new Intent(DangNhap.this, AdminMain.class));
                    progressDialog.dismiss();
                }
                else{
                    //Snackbar.make(layout,"Đăng nhập thành công: Khách hàng", BaseTransientBottomBar.LENGTH_LONG).setBackgroundTint(R.color.colorPrimary).show();
                   startActivity(new Intent(DangNhap.this, CustomerMain.class));
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        };
        user.addValueEventListener(listener);
    }
    public void mapping(){
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnDangKy   = findViewById(R.id.btnDangKy);
        btnQuenMatKhau = findViewById(R.id.btnForgetPass);
        layout = findViewById(R.id.dangnhap);
    }


}