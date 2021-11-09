package com.example.food_order_demo.user_interface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.food_order_demo.R;
import com.example.food_order_demo.adapter.FoodAdapter;
import com.example.food_order_demo.model.Foods;
import com.example.food_order_demo.model.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantActivity extends AppCompatActivity {

    Restaurant currentRestaurant;
    ArrayList<Foods> foods;
    FoodAdapter adapter;
    ListView dsMonAn;
    DatabaseReference restaurantRef;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_dsmonan);

        Toolbar toolbar =  findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tên quán ăn");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            currentRestaurant = (Restaurant) b.getSerializable("restaurant");
            getSupportActionBar().setTitle(currentRestaurant.getRestaurantName());
        }

        mapping();

        foods = new ArrayList<>();

        adapter = new FoodAdapter(this,foods);
        TextView emptyText = (TextView)findViewById(android.R.id.empty);
        dsMonAn.setAdapter(adapter);
        dsMonAn.setEmptyView(emptyText);
        restaurantRef =  FirebaseDatabase.getInstance()
                .getReference("Restaurants")
                .child(currentRestaurant.getRestaurantID())
                .child("details").child("Food");
        restaurantRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foods.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    //getKey để lấy food_id
                    Foods food = ds.getValue(Foods.class);
                    Log.i("Res activity",food.getFoodName());
                    foods.add(food);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dsMonAn.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this,FoodDetailUser.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Food",foods.get(position));
            bundle.putString("restaurantID",currentRestaurant.getRestaurantID());
            bundle.putString("restaurantName",currentRestaurant.getRestaurantName());
            intent.putExtras(bundle);
            startActivity(intent);
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void mapping() {
        dsMonAn = findViewById(R.id.dsMonAn);
    }
}
