package com.example.food_order_demo.quanlydanhmuc;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_order_demo.DangNhap;
import com.example.food_order_demo.R;
import com.example.food_order_demo.adapter.CategoryAdapter;
import com.example.food_order_demo.model.Category;
import com.example.food_order_demo.user_interface.CustomerMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    //firebase
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    private View view;
    CategoryAdapter adapter;
    DatabaseReference category;
    ArrayList<Category> categories = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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
        view = inflater.inflate(R.layout.admin_category, container, false);
        //Init Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Restaurants").child(mAuth.getCurrentUser().getUid()).child("details").child("Category");
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageReference = storage.getReference();

        //Init view - Load category
        ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);
        ListView listView = view.findViewById(R.id.listDMMA);
        TextView empty = view.findViewById(R.id.empty);
        adapter = new CategoryAdapter(getActivity(), categories);
        listView.setAdapter(adapter);
        listView.setEmptyView(empty);
        category.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                categories.clear();
                for(DataSnapshot ds:snapshot.getChildren())//category
                {
                    //getKey để lấy category_id
                    categories.add(new Category(ds.getKey(),ds.child("categoryName").getValue(String.class)));


                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transection =getFragmentManager().beginTransaction();
                EditCategoryFragment fragment = new EditCategoryFragment(); // replace your custom fragment class
                Bundle bundle = new Bundle();
                bundle.putString("categoryID",categories.get(position).getCategoryID());
                bundle.putString("categoryName",categories.get(position).getCategoryName()); // use as per your need
                fragment.setArguments(bundle);
                transection.replace(R.id.mainLayout, fragment);
                transection.commit();
            }
        });


        return view;
    }

    private void goToAddCategory(){
        getFragmentManager().beginTransaction().replace(R.id.mainLayout,new AddCategoryFragment()).commit();
    }



}