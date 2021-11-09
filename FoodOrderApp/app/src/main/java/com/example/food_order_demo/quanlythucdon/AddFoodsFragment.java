package com.example.food_order_demo.quanlythucdon;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.food_order_demo.R;
import com.example.food_order_demo.model.Facade.Facade;
import com.example.food_order_demo.model.Foods;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFoodsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFoodsFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE_REQUEST = 999;
    private static final int RESULT_OK = -1;

    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference food;
    Button btnThem, btnChooseImage, btnBack;
    ImageView foodimg;
    EditText edtNameFood, edtNameGiaBan, edtNameMoTa, edtNameDanhMuc;
    Spinner categorySpinner;
    Uri imgUri = null;
    String danhmuc;
    StorageReference storageReference;

    private View view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFoodsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFoodsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFoodsFragment newInstance(String param1, String param2) {
        AddFoodsFragment fragment = new AddFoodsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void mapping(View view){
        btnThem         = (Button) view.findViewById(R.id.btnThem);
        btnChooseImage  = (Button) view.findViewById(R.id.btnChooseImage);
        edtNameFood     = view.findViewById(R.id.edtNameFood);
        edtNameGiaBan   = view.findViewById(R.id.edtNameGiaban);
        edtNameMoTa     = view.findViewById(R.id.edtNameMoTa);
        edtNameDanhMuc  = view.findViewById(R.id.edit_danhmuc);
        categorySpinner = view.findViewById(R.id.spinner_danhmuc);
        foodimg = view.findViewById(R.id.addfoodimg);
        btnBack = view.findViewById(R.id.btnBack);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.admin_themmonan, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Thêm món ăn");
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        food = database.getReference("Restaurants").child(mAuth.getCurrentUser().getUid()).child("details").child("Food");
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        storageReference = FirebaseStorage.getInstance().getReference("Foods");
        btnThem.setOnClickListener(this);
        btnChooseImage.setOnClickListener(this::onClick);
        btnBack.setOnClickListener(v -> {
            goToFoodsFragment();
        });
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.danhmuc, R.layout.spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(arrayAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                danhmuc = text;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnThem)
        {
            ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Adding...", "Please wait...", true);
            String name = edtNameFood.getText().toString();
            String gia = edtNameGiaBan.getText().toString();
            String mota = edtNameMoTa.getText().toString();
            Foods newFood = new Foods(edtNameFood.getText().toString(),edtNameFood.getText().toString(),"",gia,mota, danhmuc);
            if (!name.isEmpty() && !gia.isEmpty() && !mota.isEmpty())
            {
                if(imgUri!=null)
                {
                    progressDialog.show();
                    StorageReference fileRef = storageReference.child(System.currentTimeMillis()
                            +"."+getFileExtension(imgUri));
                    fileRef.putFile(imgUri).addOnSuccessListener(taskSnapshot -> {
                        fileRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    newFood.setFoodImage(uri.toString());
                                    Facade.getInstance().themMonAn(mAuth.getCurrentUser().getUid(),newFood);
                                    Log.i("AddFood","add xong");
                                    progressDialog.dismiss();
                                    goToFoodsFragment();
                                });
                    });
                }
                else
                {
                    progressDialog.show();
                    Facade.getInstance().themMonAn(mAuth.getCurrentUser().getUid(),newFood);
                    Log.i("AddFood","add xong");
                    progressDialog.dismiss();
                    goToFoodsFragment();
                }
            }
            else{
                if(name.isEmpty())
                    edtNameFood.setError("Vui lòng điền thông tin.");
                if(gia.isEmpty())
                    edtNameGiaBan.setError("Vui lòng điền giá");
                if(mota.isEmpty())
                    edtNameMoTa.setError("Vui lòng điền thông tin");
                Toast.makeText(getActivity(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v.getId() == R.id.btnChooseImage)
        {
            OpenFileChoser();
        }

    }

//    private void UploadMonAn(Foods newFood) {
//        food.child(newFood.getFoodID()).setValue(newFood).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                goToFoodsFragment();
//            }
//        })
//        .addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull @NotNull Exception e) {
//                Toast.makeText(getContext(),"Add food failed",Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private void OpenFileChoser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private void goToFoodsFragment(){
        getFragmentManager().beginTransaction().replace(R.id.mainLayout,new FoodsFragment()).commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data!=null && data.getData()!=null)
        {
            imgUri = data.getData();
            foodimg.setImageURI(imgUri);
        }
    }




}