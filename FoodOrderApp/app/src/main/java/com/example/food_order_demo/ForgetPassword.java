package com.example.food_order_demo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    Toolbar toolbar;
    TextInputEditText edUserNameEmail;
    Button btnFGPass;
    public ProgressBar progressbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpassword);
        mapping();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sigin = new Intent(ForgetPassword.this, DangNhap.class);
                startActivity(sigin);
            }
        });
        btnFGPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressbar.setVisibility(View.VISIBLE);

                String email = edUserNameEmail.getText().toString().trim();
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(ForgetPassword.this,"Vui lòng nhập email",Toast.LENGTH_SHORT).show();
                    edUserNameEmail.setError("Vui lòng nhập email");
                }
                else {
                    resetPassword(email);
                }
            }
        });
    }
    public void mapping(){
        toolbar = findViewById(R.id.toolbar_FGP);
        edUserNameEmail = findViewById(R.id.edtUserNameEmail);
        btnFGPass = findViewById(R.id.btnFGPass);
    }
    public void resetPassword(String email){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                btnFGPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressbar.setVisibility(View.VISIBLE);
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                showDialog();
                            } else {
                                Toast.makeText(ForgetPassword.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                            progressbar.setVisibility(View.GONE);

                        }
                    });
                }
            });



    }
    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.resetpassword_dialog);
        //mapping
        Button btnOK = (Button) dialog.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
