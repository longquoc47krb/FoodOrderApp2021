package com.example.food_order_demo;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener{

    String Uid;
    FirebaseAuth mAuth;
    DatabaseReference table_user;
    EditText edtOldPassword, edtNewPassword, edtConfirmPW;
    Button btnDoiMatKhau;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doimatkhau);

        mapping();
        Toolbar toolbar =  findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnDoiMatKhau.setOnClickListener(this::onClick);
    }

    private void mapping() {
        edtOldPassword = findViewById(R.id.old_pasword);
        edtNewPassword = findViewById(R.id.new_password);
        edtConfirmPW = findViewById(R.id.confirm_password);
        btnDoiMatKhau = findViewById(R.id.changePass);
    }


    private void changePassword() {
        mAuth = FirebaseAuth.getInstance();
        Uid = mAuth.getCurrentUser().getUid();
        String oldpass = edtOldPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirm = edtConfirmPW.getText().toString().trim();
        if(oldpass.isEmpty()){
            Toast.makeText(ChangePassword.this, "Vui lòng nhập mật khẩu cũ", Toast.LENGTH_SHORT).show();
            edtOldPassword.setFocusable(View.FOCUSABLE_AUTO);
        }
        else if(newPassword.isEmpty()){
            Toast.makeText(ChangePassword.this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();

            edtNewPassword.setFocusable(View.FOCUSABLE_AUTO);
        }
        else if(confirm.equals(oldpass)){
            Toast.makeText(ChangePassword.this, "Mật khẩu mới và mật khẩu cũ không được trùng nhau.", Toast.LENGTH_SHORT).show();
            edtConfirmPW.setFocusable(View.FOCUSABLE_AUTO);
        }
        else if(confirm.isEmpty()){
            Toast.makeText(ChangePassword.this, "Vui lòng xác nhận mật khẩu mới.", Toast.LENGTH_SHORT).show();
            edtConfirmPW.setFocusable(View.FOCUSABLE_AUTO);
        }
        else{
            table_user = FirebaseDatabase.getInstance().getReference("User").child(Uid);
            FirebaseUser user;
            user = mAuth.getCurrentUser();
            final String email = user.getEmail();
            AuthCredential credential = EmailAuthProvider.getCredential(email,oldpass);

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ChangePassword.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                    ChangePassword.this.onBackPressed();
                                }else {
                                    Toast.makeText(ChangePassword.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(ChangePassword.this, "Thất bại", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.changePass){
            changePassword();
        }

    }
}
