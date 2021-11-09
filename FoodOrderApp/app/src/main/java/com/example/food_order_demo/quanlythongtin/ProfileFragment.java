package com.example.food_order_demo.quanlythongtin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.example.food_order_demo.AdminHomeFragment;
import com.example.food_order_demo.AdminMain;
import com.example.food_order_demo.DangNhap;
import com.example.food_order_demo.model.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.food_order_demo.R;
import com.example.food_order_demo.model.Restaurant;
import com.example.food_order_demo.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference table_restaurant, table_user;
    private StorageReference mStorageReference;
    NavigationView navigationView;
    String Uid;
    Button btnCapNhat;
    Uri saveUri;
    ImageButton avt_img;
    private ProgressBar progressBar;
    EditText edtRestaurantName, edtUserName, edtPhone, edtEmail, edtAddress;
    private final int PICK_IMAGE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public void mapping(View view){
        btnCapNhat = view.findViewById(R.id.btnSua);
        edtUserName = view.findViewById(R.id.edtUsername_AdminEdirProfile);
        edtAddress = view.findViewById(R.id.edtDiaChi_AdminEditProfile);
        edtEmail = view.findViewById(R.id.edtEmail_AdminEditProfile);
        edtPhone = view.findViewById(R.id.edtPhone_AdminEditProfile);
        edtRestaurantName = view.findViewById(R.id.edtTenQuanAn);
        avt_img = view.findViewById(R.id.img_avatar);



    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        avt_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImageFromGallery();
            }
        });
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Lưu thay đổi? ")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       capNhat();
                       Toast.makeText(getActivity(),"Cập nhật thành công",Toast.LENGTH_LONG).show();
//                       startActivity(new Intent(getActivity(), AdminHomeFragment.class));
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void capNhat() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("address", edtAddress.getText().toString());
        hashMap.put("email", edtEmail.getText().toString());
        hashMap.put("fullName", edtRestaurantName.getText().toString());
        hashMap.put("phone",edtPhone.getText().toString());
        hashMap.put("role","admin");
        hashMap.put("userName",edtUserName.getText().toString());
        table_user.setValue(hashMap);
        table_restaurant.child(Uid).child("details").child("restaurantName").setValue(edtRestaurantName.getText().toString());
        UploadImageInStorageDataBase(saveUri);

    }

    // Xử lý ảnh
    private void loadImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh ..."), PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== PICK_IMAGE && resultCode==RESULT_OK){
            Uri imageUri= data.getData();
            Glide.with(getActivity()).load(imageUri).into(avt_img);
            avt_img.setImageURI(imageUri);
            saveUri = imageUri;
        }
    }
    private void UploadImageInStorageDataBase(Uri resultUri) {

        //upload image in storage database
        final StorageReference FilePath = FirebaseStorage.getInstance().getReference().child("Restaurant_Image").child(Uid);
        FilePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                FilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference restaurantRef = FirebaseDatabase.getInstance().getReference().child("Restaurants").child(Uid);
                        restaurantRef.child("image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(),"Cập nhật ảnh thành công",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

            }
        });


    }

    private void getNavHeaderData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.admin_chinhsuathongtin, container, false);
        mAuth = FirebaseAuth.getInstance();
        Uid = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        mStorageReference= FirebaseStorage.getInstance().getReference();
        table_restaurant = database.getReference("Restaurants").child(Uid);
        table_user = database.getReference("User").child(Uid);

        loadData();
        return view;
    }
    public void loadData(){
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                edtUserName.setText(user.getUserName());
                edtPhone.setText(user.getPhone());
                edtEmail.setText(user.getEmail());
                // Email read-only
                edtEmail.setFocusable(false);
                edtEmail.setEnabled(false);
                edtEmail.setCursorVisible(false);
                edtEmail.setKeyListener(null);
                edtEmail.setTextColor(Color.GRAY);
                //
                edtAddress.setText(user.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        table_restaurant.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Restaurant res = snapshot.getValue(Restaurant.class);
                edtRestaurantName.setText(res.getRestaurantName());
                if(res.getImage().equals("default")){
                    Glide.with(getActivity()).load(R.drawable.profile).centerCrop().into(avt_img);
                }
                else{
                    Glide.with(getActivity()).load(res.getImage()).placeholder(R.drawable.profile).centerCrop().into(avt_img);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}