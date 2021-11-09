//package com.example.food_order_demo.quanlydanhmuc;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.example.food_order_demo.R;
//import com.example.food_order_demo.adapter.CategoryAdapter;
//import com.example.food_order_demo.model.Category;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import java.util.ArrayList;
//
//public class QuanLyDanhMucFragment extends Fragment implements View.OnClickListener {
//
//    //firebase
//    FirebaseAuth mAuth;
//    FirebaseDatabase database;
//    private View view;
//    CategoryAdapter adapter;
//    DatabaseReference category;
//    ArrayList<Category> categories = new ArrayList<>();
//    Button btnThem;
//    public QuanLyDanhMucFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        mapping(view);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.admin_quanlydmma, container, false);
//        //Init Firebase
//        mAuth = FirebaseAuth.getInstance();
//        database = FirebaseDatabase.getInstance();
//        category = database.getReference("Restaurants").child(mAuth.getCurrentUser().getUid()).child("details").child("Category");
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageReference = storage.getReference();
//
//        //Init view - Load category
//        ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);
//        ListView listView = view.findViewById(R.id.listDMMA);
//        adapter = new CategoryAdapter(getActivity(), categories);
//        listView.setAdapter(adapter);
//        category.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                categories.clear();
//                for(DataSnapshot ds:snapshot.getChildren())//category
//                {
//                    //getKey để lấy category_id
//                        categories.add(new Category(ds.getKey(),ds.child("categoryName").getValue(String.class)));
//
//
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        adapter.setOnClickItemListener(new CategoryAdapter.onItemClickListener() {
//            @Override
//            public void onItemClick(int pos) {
//                Intent i = new Intent(getActivity() , EditProduct.class);
//                Bundle b = new Bundle();
//                b.putString("img" , adminProducts.get(pos).getImage());
//                b.putString("name" , adminProducts.get(pos).getName());
//                b.putString("category" , adminProducts.get(pos).getCategory());
//                b.putString("expired" , adminProducts.get(pos).getExpired());
//                b.putString("price" , adminProducts.get(pos).getPrice());
//                b.putString("quantity" , adminProducts.get(pos).getQuantity());
//                i.putExtras(b);
//                startActivity(i);
//            }
//        });
//
//        return view;
//
//    }
//    private void mapping(View view){
//        btnThem = (Button) view.findViewById(R.id.btnTDM);
//        btnThem.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btnTDM:
//                Toast.makeText(getActivity(),"Thêm danh mục",Toast.LENGTH_SHORT).show();
//                break;
//            default: break;
//        }
//    }
//}