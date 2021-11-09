package com.example.food_order_demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.food_order_demo.adapter.FoodAdapter;
import com.example.food_order_demo.model.Foods;
import com.example.food_order_demo.model.Singleton.Singleton;
import com.example.food_order_demo.quanlythucdon.EditFoodsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class AdminHomeFragment extends Fragment implements View.OnClickListener {
    Button btnTimMonAn;
    EditText edtFoodName;
    ListView listView;
    FoodAdapter adapter;
    ArrayList<Foods> foodsListAll;
    ArrayList<Foods> foodsListSearch;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        btnTimMonAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edtFoodName.getText().toString();
                if(text.isEmpty())
                {
                    foodsListSearch.clear();
                    foodsListSearch.addAll(foodsListAll);
                    Log.d("AdminHome", foodsListSearch.size()+" size");
                    adapter.notifyDataSetChanged();
                }
                else {
                    foodsListSearch.clear();
                    foodsListSearch.addAll(filter(foodsListAll,text));
                    Log.d("AdminHome", foodsListSearch.size()+" size");
                    adapter.notifyDataSetChanged();

                }

            }
        });

        foodsListAll = new ArrayList<>();
        foodsListSearch = new ArrayList<>();
        adapter = new FoodAdapter(getContext(),foodsListSearch);
        TextView textView = view.findViewById(R.id.empty);
        listView.setEmptyView(textView);
        listView.setAdapter(adapter);

        getAllFoods();
    }

    private ArrayList<Foods> filter(ArrayList<Foods> foodsListAll, String text) {
        text = text.toLowerCase(Locale.getDefault());
        ArrayList<Foods> foodsListReturn = new ArrayList<>();
        if(text.length() == 0)
        {
            foodsListReturn.addAll(foodsListAll);
            return foodsListReturn;
        }
        else {
            for(Foods food:foodsListAll)
            {
                String name = food.getFoodName().toLowerCase(Locale.getDefault());
                if(name.contains(text))
                    foodsListReturn.add(food);
            }
        }
        return foodsListReturn;

    }

    private void getAllFoods() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference food = database.getReference("Restaurants").child(Singleton.getInstance().getUserID()).child("details").child("Food");
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageReference = storage.getReference();

        //Init view - Load category
        food.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodsListAll.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    //getKey để lấy food_id
                    Foods food = ds.getValue(Foods.class);
                    foodsListAll.add(food);
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
                bundle.putString("foodID",foodsListAll.get(position).getFoodID());
                bundle.putString("foodName",foodsListAll.get(position).getFoodName());
                bundle.putString("foodPrice",foodsListAll.get(position).getPrice());
                bundle.putString("foodDescription",foodsListAll.get(position).getDescription());
                bundle.putString("foodCategory",foodsListAll.get(position).getCategory());
                bundle.putString("foodImage",foodsListAll.get(position).getFoodImage());

                fragment.setArguments(bundle);
                transection.replace(R.id.mainLayout, fragment);
                transection.commit();
            }

        });
    }

    private void mapping(View view) {
        edtFoodName = (EditText) view.findViewById(R.id.edtFoodName);
        btnTimMonAn = (Button) view.findViewById(R.id.btnTimMonAn);
        listView = view.findViewById(R.id.listviewmonan);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnTimMonAn)
        {

        }
    }
}
