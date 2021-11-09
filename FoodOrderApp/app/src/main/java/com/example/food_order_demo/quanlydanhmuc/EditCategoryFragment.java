package com.example.food_order_demo.quanlydanhmuc;

import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_order_demo.R;
import com.example.food_order_demo.adapter.CategoryAdapter;
import com.example.food_order_demo.model.Category;
import com.example.food_order_demo.model.Facade.Facade;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditCategoryFragment extends Fragment implements View.OnClickListener {

    Button btnXoa, btnSua;
    EditText edtTenDanhMuc;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference category;
    String categoryID, categoryName;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditCategoryFragment newInstance(String param1, String param2) {
        EditCategoryFragment fragment = new EditCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, param1);
        bundle.putString(ARG_PARAM2, param2);
        fragment.setArguments(bundle);
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
        View view = inflater.inflate(R.layout.admin_edit_category, container, false);
        mapping(view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Sửa danh mục");
        // Init firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Restaurants").child(mAuth.getCurrentUser().getUid()).child("details").child("Category");

        // Load dữ liệu từ bundle nhận đc từ CategoryFragment

        Bundle bundle = getArguments();
        categoryName = String.valueOf(bundle.getString("categoryName"));
        edtTenDanhMuc.setText(categoryName);
        categoryID = String.valueOf(bundle.getString("categoryID"));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        btnXoa.setOnClickListener(this);
    }

    private void mapping(View view) {
        btnXoa = view.findViewById(R.id.btnXoaDanhMuc);
        edtTenDanhMuc = view.findViewById(R.id.editTenDanhMuc);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnXoaDanhMuc){
            showDialog();
        }

    }
    private void goToCategoryFragment(){
        getFragmentManager().beginTransaction().replace(R.id.mainLayout,new CategoryFragment()).commit();
    }

    private void showDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_thongbaoxacnhan);
        //mapping
        TextView txtThongBao = dialog.findViewById(R.id.dialog_thongbao);
        TextView txtNoiDung = dialog.findViewById(R.id.dialog_content);
        Button btnXacNhan = (Button) dialog.findViewById(R.id.btnXacNhan);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnHuy);

        txtThongBao.setText("Xóa danh mục");
        txtNoiDung.setText("Xóa danh mục này cũng sẽ xóa toàn bộ các món ăn thuộc danh mục này của quán\n Bạn có chắc chắn muốn xóa không?");
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Facade.getInstance().xoaDanhMuc(mAuth.getCurrentUser().getUid(),categoryName);
                //category.child(categoryID).removeValue();
                dialog.dismiss();
                Toast.makeText(getActivity(), "Danh mục đã được xóa !!!", Toast.LENGTH_SHORT);
                goToCategoryFragment();
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
}