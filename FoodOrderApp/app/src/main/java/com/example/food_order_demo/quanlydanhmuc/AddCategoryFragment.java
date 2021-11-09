package com.example.food_order_demo.quanlydanhmuc;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.food_order_demo.R;
import com.example.food_order_demo.model.Category;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCategoryFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference category;
    Button btnThem;
    EditText edtThemDanhMuc;
    private View view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCategoryFragment newInstance(String param1, String param2) {
        AddCategoryFragment fragment = new AddCategoryFragment();
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
        btnThem = (Button) view.findViewById(R.id.btnThemDanhMuc);
        edtThemDanhMuc = view.findViewById(R.id.edtThemDanhMuc);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.admin_add_category, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Thêm danh mục");
         mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Restaurants").child(mAuth.getCurrentUser().getUid()).child("details").child("Category");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        btnThem.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnThemDanhMuc)
        {
            Category newCategory = new Category(edtThemDanhMuc.getText().toString());
            if (newCategory != null) {
                // Thêm danh mục mới vào db, push để set category id
                category.push().setValue(newCategory);
                Toast.makeText(getActivity(), "Danh mục " + newCategory.getCategoryName() + " đã được thêm", Toast.LENGTH_SHORT).show();
                goToCategoryFragment();
            }
            else{
                edtThemDanhMuc.setError("Vui lòng điền thông tin.");
                Toast.makeText(getActivity(), "Thêm thất bại", Toast.LENGTH_SHORT).show();

            }
        }

    }
    private void goToCategoryFragment(){
        getFragmentManager().beginTransaction().replace(R.id.mainLayout,new CategoryFragment()).commit();
    }
}