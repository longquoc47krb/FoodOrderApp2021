package com.example.food_order_demo.quanlythucdon;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.food_order_demo.R;
import com.example.food_order_demo.adapter.FoodAdapter;
import com.example.food_order_demo.common.Common;
import com.example.food_order_demo.model.Foods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodsFragment extends Fragment implements View.OnClickListener{
    //firebase
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    private View view;
    FoodAdapter adapter;
    DatabaseReference food;
    ArrayList<Foods> foods = new ArrayList<>();
    Button btnThem;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FoodsFragment() {
        // Required empty public constructor
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        btnThem.setOnClickListener(this);
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodsFragment newInstance(String param1, String param2) {
        FoodsFragment fragment = new FoodsFragment();
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
        btnThem = (Button) view.findViewById(R.id.btnTMA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_foods, container, false);
        //Init Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        food = database.getReference("Restaurants").child(mAuth.getCurrentUser().getUid()).child("details").child("Food");
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageReference = storage.getReference();

        //Init view - Load category
        ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);
        ListView listView = view.findViewById(R.id.listviewquanlymonan);
        TextView empty = view.findViewById(R.id.empty);
        adapter = new FoodAdapter(getActivity(), foods);
        listView.setAdapter((ListAdapter) adapter);
        listView.setEmptyView(empty);
        food.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                foods.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    //getKey để lấy food_id
                    Foods food = ds.getValue(Foods.class);
                    foods.add(food);
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
                EditFoodsFragment fragment = new EditFoodsFragment(); // replace your custom fragment class
                Bundle bundle = new Bundle();
                bundle.putString("foodID",foods.get(position).getFoodID());
                bundle.putString("foodName",foods.get(position).getFoodName());
                bundle.putString("foodPrice", foods.get(position).getPrice());
                bundle.putString("foodDescription",foods.get(position).getDescription());
                bundle.putString("foodCategory",foods.get(position).getCategory());
                bundle.putString("foodImage",foods.get(position).getFoodImage());

                fragment.setArguments(bundle);
                transection.replace(R.id.mainLayout, fragment);
                transection.commit();
            }

        });

        return view;
    }
    private void goToAddFood(){
        getFragmentManager().beginTransaction().replace(R.id.mainLayout,new AddFoodsFragment()).commit();
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnTMA){
            goToAddFood();
        }
    }

}