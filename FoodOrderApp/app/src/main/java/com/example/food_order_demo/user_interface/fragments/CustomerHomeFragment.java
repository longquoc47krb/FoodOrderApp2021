package com.example.food_order_demo.user_interface.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_order_demo.R;
import com.example.food_order_demo.adapter.RestaurantAdapter;
import com.example.food_order_demo.model.Category;
import com.example.food_order_demo.model.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CustomerHomeFragment extends Fragment {

    private List<Restaurant> restaurants;
    private List<Restaurant> restaurantsSearch;
    private RecyclerView recyclerView;
    RestaurantAdapter adapter;
    DatabaseReference table_restaurant;
    SearchView searchView;
    Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.customer_fragment_home, container, false);
//       // getRestaurant();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //mapping
        recyclerView = view.findViewById(R.id.ListQuanAn);
        searchView = view.findViewById(R.id.search_view);
        mToolbar = view.findViewById(R.id.topAppBar);
        //

        final FragmentActivity fragmentActivity = getActivity();
        recyclerView.setHasFixedSize(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.ListQuanAn);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragmentActivity));
        table_restaurant = FirebaseDatabase.getInstance().getReference().child("Restaurants");

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);


        restaurants = new ArrayList<>();
        restaurantsSearch = new ArrayList<>();
        adapter = new RestaurantAdapter(fragmentActivity,restaurantsSearch);
        recyclerView.setAdapter(adapter);

        table_restaurant = FirebaseDatabase.getInstance().getReference("Restaurants");
        table_restaurant.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                restaurants.clear();
                for(DataSnapshot child : snapshot.getChildren()){
                    Restaurant restaurant = child.getValue(Restaurant.class);
                    restaurant.setRestaurantID(child.getKey());
                    DatabaseReference cateRef = table_restaurant.child(restaurant.getRestaurantID()).child("details").child("Category");
                    cateRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            restaurant.danhmucList = new ArrayList<>();
                            for(DataSnapshot ds:snapshot.getChildren())//category
                            {
                                //getKey để lấy category_id
                                String danhmuc = ds.child("categoryName").getValue(String.class);
                                restaurant.danhmucList.add(danhmuc);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        }
                    });
                    restaurants.add(restaurant);
                    restaurantsSearch.add(restaurant);
                    adapter.notifyDataSetChanged();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter.notifyDataSetChanged();
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.isEmpty()) {
                    Log.i("CustomerHome","text not empty");
                    restaurantsSearch.clear();
                    restaurantsSearch.addAll(filter(restaurants, newText));
                    adapter.notifyDataSetChanged();
                }
                else {
                    Log.i("CustomerHome","text empty");
                    restaurantsSearch.clear();
                    restaurantsSearch.addAll(restaurants);
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
        });

    }

    private List<Restaurant> filter(List<Restaurant> restaurants, String text) {
        text = text.toLowerCase(Locale.getDefault());
        List<Restaurant> restaurantsNew = new ArrayList<>();
        if(text.length()==0)
        {
            restaurantsNew.addAll(restaurants);
            return restaurantsNew;
        }

        for (Restaurant restaurant:restaurants)
        {
            String name = restaurant.getRestaurantName().toLowerCase(Locale.getDefault());
            Log.i("CustomerHome",name +"-"+text);
            if(name.contains(text))
                restaurantsNew.add(restaurant);
        }
        Log.i("CustomerHome",restaurantsNew.size()+" size");
        return restaurantsNew;
    }



    /* private void getRestaurant() {

        DatabaseReference table_restaurant = FirebaseDatabase.getInstance().getReference("Restaurants");
        table_restaurant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot child: snapshot.getChildren()){
                        restaurants.add(new Restaurant(child.child("image").getValue(String.class),child.child("restaurantName").getValue(String.class)));
                    }
                }
                adapter = new RestaurantAdapter(restaurants, getActivity());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
}
