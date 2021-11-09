package com.example.food_order_demo.quanlythucdon;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.food_order_demo.R;
import com.example.food_order_demo.model.Facade.Facade;
import com.example.food_order_demo.model.Foods;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFoodsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFoodsFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE_REQUEST = 999;
    private static final int RESULT_OK = -1;

    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference food;
    Button btnSua, btnXoa, btnChooseImage, btnBack;
    EditText edtNameFood, edtNameGiaBan, edtNameMoTa;
    Spinner edtNameDanhMuc;
    ImageView foodimg;
    Uri imgUri = null;
    String foodID;
    String danhmuc;
    Foods foodEdit;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private StorageReference storageReference;

    public EditFoodsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditFoodsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFoodsFragment newInstance(String param1, String param2) {
        EditFoodsFragment fragment = new EditFoodsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_thongtinmonan, container, false);
        mapping(view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Sửa món ăn");
        // Init firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        food = database.getReference("Restaurants").child(mAuth.getCurrentUser().getUid()).child("details").child("Food");

        // Load dữ liệu từ bundle nhận đc từ FoodFragment

        Bundle bundle = getArguments();
        foodEdit = new Foods();
        foodEdit.setFoodID(String.valueOf(bundle.getString("foodID")));
        foodEdit.setFoodName(String.valueOf(bundle.getString("foodName")));
        foodEdit.setFoodImage(String.valueOf(bundle.getString("foodImage")));
        foodEdit.setCategory(String.valueOf(bundle.getString("foodCategory")));
        foodEdit.setDescription(String.valueOf(bundle.getString("foodDescription")));
        foodEdit.setPrice(String.valueOf(bundle.getString("foodPrice")));

        foodID = String.valueOf(bundle.getString("foodID"));
        edtNameFood.setText(String.valueOf(bundle.getString("foodName")));
        edtNameGiaBan.setText(String.valueOf(bundle.getString("foodPrice")));
        edtNameMoTa.setText(String.valueOf(bundle.getString("foodDescription")));
        Glide.with(getContext()).load(foodEdit.getFoodImage()).into(foodimg);


//        edtNameDanhMuc.setText(String.valueOf(bundle.getString("foodCategory")));
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.danhmuc, R.layout.spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtNameDanhMuc.setAdapter(arrayAdapter);
        edtNameDanhMuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(),text,Toast.LENGTH_SHORT).show();
                danhmuc = text;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
         return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        btnSua.setOnClickListener(this);
        btnXoa.setOnClickListener(this);
        btnBack.setOnClickListener(v -> {
            goToFoodsFragment();
        });
    }

    private void mapping(View view) {
        btnSua = view.findViewById(R.id.btnSuaMonAn);
        btnXoa = view.findViewById(R.id.btnXoa);
        btnChooseImage = view.findViewById(R.id.btnChooseImage);
        edtNameFood = view.findViewById(R.id.edtNameFood);
        edtNameGiaBan = view.findViewById(R.id.edtNameGiaban);
        edtNameMoTa = view.findViewById(R.id.edtNameMoTa);
        foodimg = view.findViewById(R.id.imgFood_edit);
        edtNameDanhMuc = view.findViewById(R.id.spnCategory);
        storageReference = FirebaseStorage.getInstance().getReference("Foods");
        btnBack = view.findViewById(R.id.btnBack);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSuaMonAn){
            ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Editing food...", "Please wait...", true);

            Foods updateFoods = foodEdit;
            //updateFoods.setFoodName(edtNameFood.getText().toString());
            String name = edtNameFood.getText().toString();
            String gia = edtNameGiaBan.getText().toString();
            String mota = edtNameMoTa.getText().toString();

            if (!name.isEmpty() && !gia.isEmpty() && !mota.isEmpty()) {
                if (imgUri!=null) {
                    updateFoods.setFoodName(name);
                    updateFoods.setPrice(gia);
                    updateFoods.setDescription(mota);
                    updateFoods.setCategory(danhmuc);
                    progressDialog.show();
                    StorageReference fileRef = storageReference.child(System.currentTimeMillis()
                            + "." + getFileExtension(imgUri));
                    fileRef.putFile(imgUri).addOnSuccessListener(taskSnapshot -> {
                        fileRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    updateFoods.setFoodImage(uri.toString());
                                    EditMonAn(updateFoods);
                                    progressDialog.dismiss();
                                });
                    });

                }
                else
                {
                    progressDialog.show();
                    updateFoods.setFoodName(name);
                    updateFoods.setPrice(gia);
                    updateFoods.setDescription(mota);
                    updateFoods.setCategory(danhmuc);
                    EditMonAn(updateFoods);
                    progressDialog.dismiss();
                }
            }

            else {
                if(name.isEmpty())
                    edtNameFood.setError("Vui lòng điền thông tin.");
                if(gia.isEmpty())
                    edtNameGiaBan.setError("Vui lòng điền giá");
                if(mota.isEmpty())
                    edtNameMoTa.setError("Vui lòng điền thông tin");
                Toast.makeText(getActivity(), "Sửa thất bại", Toast.LENGTH_SHORT).show();
            }
        }

        else if(v.getId() == R.id.btnXoa)
        {
            showDialog();
        }

        else if(v.getId() == R.id.btnChooseImage)
        {
            OpenFileChoser();
        }

    }



    private void showDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_thongbaoxacnhan);
        //mapping
        TextView txtThongBao = dialog.findViewById(R.id.dialog_thongbao);
        TextView txtNoiDung = dialog.findViewById(R.id.dialog_content);
        Button btnXacNhan = (Button) dialog.findViewById(R.id.btnXacNhan);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnHuy);

        txtThongBao.setText("Xóa món ăn");
        txtNoiDung.setText("Bạn có chắc chắn muốn xóa món ăn này không");
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Deleting food...", "Please wait...", true);
                progressDialog.show();
                Facade.getInstance().xoaMonAn(mAuth.getCurrentUser().getUid(),foodID);
                progressDialog.dismiss();
                dialog.dismiss();
                goToFoodsFragment();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void EditMonAn(Foods updateFoods) {
        food.child(foodID).setValue(updateFoods).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Facade.getInstance().capNhatDanhMucQuanAn(mAuth.getCurrentUser().getUid());
                goToFoodsFragment();
            }
        });

    }

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
        getActivity().onBackPressed();
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