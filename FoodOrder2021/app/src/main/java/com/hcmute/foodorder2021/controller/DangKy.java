package com.hcmute.foodorder2021.controller;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hcmute.foodorder2021.R;
import com.hcmute.foodorder2021.common.Utils;
import com.hcmute.foodorder2021.models.Restaurant;
import com.hcmute.foodorder2021.models.User;

import java.util.HashMap;
import java.util.regex.Pattern;

public class DangKy extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 71;
    EditText edtFullName, edtUserName, edtPhone, edtEmail, edtAddress, edtPassword, edtRePassword;
    RadioGroup Vaitro;
    Toolbar toolbar;
    LinearLayout layout;
    RadioButton radioBtnNguoiMua, radioBtnNguoiBan, radioBtnShipper;
    Button btnDangKy, btnDangNhap;
    public String role, uId;
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRootRef = database.getReference();
    DatabaseReference restaurantRef = myRootRef.child("Restaurants");
    DatabaseReference shipperRef = myRootRef.child("Shippers");
    DatabaseReference userRef = myRootRef.child("User");

    Uri imgUri = null;
    Button btnChooseImage;
    ImageView foodimg ;

    boolean isNameValid, isEmailValid, isPhoneValid, isPasswordValid,isAddressValid;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void reload() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangky);
        mapping();
        role = "customer";
        mAuth = FirebaseAuth.getInstance();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sigin = new Intent(DangKy.this, DangNhap.class);
                startActivity(sigin);
            }
        });
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin = new Intent(DangKy.this, DangNhap.class);
                startActivity(signin);
            }
        });
    }

    public void mapping(){
        edtFullName = findViewById(R.id.edtName);
        edtUserName = findViewById(R.id.edtUsername);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtRePassword = findViewById(R.id.edtRePassword);
        edtAddress = findViewById(R.id.edtAddress);
        btnDangKy = findViewById(R.id.btnSignUp);
        btnDangNhap = findViewById(R.id.btnSignIn);
        toolbar = findViewById(R.id.admin_toolbar);
        Vaitro = findViewById(R.id.vaitro);
        radioBtnNguoiBan = findViewById(R.id.radioBtnNguoiBan);
        radioBtnNguoiMua = findViewById(R.id.radioBtnNguoiMua);
        radioBtnShipper = findViewById(R.id.radioBtnShipper);
        layout = findViewById(R.id.dangky);

    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioBtnNguoiMua:
                if (checked)
                    role = "customer";
                break;
            case R.id.radioBtnNguoiBan:
                if (checked)
                    role = "admin";
                break;
            case R.id.radioBtnShipper:
                if (checked)
                    role = "shipper";
                break;
            default: role = "customer";
        }
    }
    public void createUser() {
        // Check for a valid name
        String fullName = edtFullName.getText().toString();
        String address = edtAddress.getText().toString();
        String phone = edtPhone.getText().toString();
        String password = edtPassword.getText().toString();
        String userName = edtUserName.getText().toString();
        String email = edtEmail.getText().toString();
        if (fullName.isEmpty()) {
            edtFullName.setError("Vui lòng nhập thông tin!");
            isNameValid = false;
        } else {
            isNameValid = true;

        }
        if (address.isEmpty()) {

            edtAddress.setError("Vui lòng nhập thông tin!");
            isAddressValid = false;
        } else {
            isAddressValid = true;
        }
        // Check for a valid email address.
        if (email.isEmpty()) {
            edtEmail.setError("Vui lòng nhập email");
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
            edtEmail.setError("Email không hợp lệ");
            isEmailValid = false;
        } else {
            isEmailValid = true;
        }

        // Check for a valid phone number.
        if (phone.isEmpty()) {
            edtPhone.setError("Vui lòng nhập sđt");
            isPhoneValid = false;
        } else {
            isPhoneValid = true;
        }
        // Check for a valid password.
        if (password.isEmpty()) {
            edtPassword.setError("Không được để trống");
            isPasswordValid = false;
        } else if (password.length() < 6) {
            edtPassword.setError("Password không được ít hơn 6 kí tự");
            isPasswordValid = false;
        } else {
            isPasswordValid = true;
        }
        boolean pwdMatch = false;
        if (!edtRePassword.getText().toString().equals(edtPassword.getText().toString()))
            edtRePassword.setError("Passwords does not match!");
        else {
            pwdMatch = true;
        }
        if (isNameValid && isEmailValid && isPhoneValid && isPasswordValid && pwdMatch) {
                showDialogChooseAvatr(email,password);

        }
    }
    public void signUp(String email, String password){
        String fullName = edtFullName.getText().toString();
        String address = edtAddress.getText().toString();
        String phone = edtPhone.getText().toString();
        password = edtPassword.getText().toString();
        String userName = edtUserName.getText().toString();
        email = edtEmail.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        if(imgUri!=null)
                                        {
                                            StorageReference storageReference;
                                            if(role == "admin"){
                                                storageReference = FirebaseStorage.getInstance().getReference("Restaurant_Image");
                                            }
                                            else if(role == "shipper"){
                                                storageReference = FirebaseStorage.getInstance().getReference("Shipper_Image");
                                            }
                                            else{
                                                storageReference = FirebaseStorage.getInstance().getReference("User_Image");
                                            }
                                            StorageReference fileRef = storageReference.child(System.currentTimeMillis()
                                                    +"."+getFileExtension(imgUri));
                                            fileRef.putFile(imgUri).addOnSuccessListener(taskSnapshot -> {
                                                fileRef.getDownloadUrl()
                                                        .addOnSuccessListener(uri -> {
                                                            HashMap<String, String> hashMap = new HashMap<>();
                                                            hashMap.put("userName",userName);
                                                            hashMap.put("phone",phone);
                                                            hashMap.put("password",edtPassword.getText().toString());
                                                            hashMap.put("fullName", fullName);
                                                            hashMap.put("email",edtEmail.getText().toString());
                                                            hashMap.put("address", address);
                                                            hashMap.put("role",role);
                                                            hashMap.put("image",uri.toString());
                                                            FirebaseUser currentUser = mAuth.getCurrentUser();
                                                            uId = currentUser.getUid();
                                                            if(role.equals("admin")) {
                                                                Restaurant restaurant = new Restaurant(uId, uri.toString(), fullName);
                                                                restaurantRef.child(uId).setValue(restaurant);
                                                            }
                                                            if(role.equals("shipper")){
                                                                User shipper = new User(userName, phone, edtPassword.getText().toString(), fullName, edtEmail.getText().toString(), address, role, uri.toString(), null);
                                                                shipperRef.child(uId).setValue(shipper);
                                                                userRef.child(uId).setValue(shipper);
                                                            }
                                                            DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("User");
                                                            df.child(uId).setValue(hashMap);
                                                            int secs = 1; // Delay in seconds

                                                            Utils.delay(secs, new Utils.DelayCallback() {
                                                                @Override
                                                                public void afterDelay() {
                                                                    Snackbar.make(layout, "Đăng ký thành công", BaseTransientBottomBar.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                         //   SendUser2LoginActivity();
                                                        });
                                            });
                                        }
                                        else
                                        {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap.put("userName",userName);
                                            hashMap.put("phone",phone);
                                            hashMap.put("password",edtPassword.getText().toString());
                                            hashMap.put("fullName", fullName);
                                            hashMap.put("email",edtEmail.getText().toString());
                                            hashMap.put("address", address);
                                            hashMap.put("role",role);
                                            hashMap.put("image","default");
                                            FirebaseUser currentUser = mAuth.getCurrentUser();
                                            uId = currentUser.getUid();
                                            if(role.equals("admin")){
                                                Restaurant restaurant = new Restaurant("default",fullName);
                                                restaurantRef.child(uId).setValue(restaurant);
                                            }
                                            if(role.equals("shipper")){
                                                User shipper = new User(userName, phone, edtPassword.getText().toString(), fullName, edtEmail.getText().toString(), address, role,"default", null);
                                                shipperRef.child(uId).setValue(shipper);
                                                userRef.child(uId).setValue(shipper);

                                            }
                                            DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("User");
                                            df.child(uId).setValue(hashMap);
                                            Snackbar.make(layout, "Đăng ký thành công", BaseTransientBottomBar.LENGTH_SHORT).show();
                                           // SendUser2LoginActivity();
                                        }


                                    }
                                });




                    }


                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(DangKy.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showDialogChooseAvatr(String email, String password) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_choose_avatar);
        //
        Button btnXacNhan = (Button) dialog.findViewById(R.id.btnXacNhan);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnHuy);
        btnChooseImage = dialog.findViewById(R.id.btnChooseImage);
        foodimg = dialog.findViewById(R.id.img_avatar);
        imgUri = null;
        //
        btnChooseImage.setOnClickListener(v -> {
            OpenFileChoser();
        });
        btnXacNhan.setOnClickListener(v -> {

            signUp(email,password);
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private void OpenFileChoser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data!=null && data.getData()!=null)
        {
            imgUri = data.getData();
            foodimg.setImageURI(imgUri);
        }
    }

    private void SendUser2LoginActivity() {
        Intent intent = new Intent(DangKy.this,DangNhap.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}