package com.example.food_order_demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_order_demo.R;
import com.example.food_order_demo.model.Restaurant;
import com.example.food_order_demo.user_interface.RestaurantActivity;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private Context context;
    private List<Restaurant> restaurants;

    public RestaurantAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_nhahang, parent, false);
        return new RestaurantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Restaurant restaurant = restaurants.get(position);
        holder.restaurantName.setText(restaurant.getRestaurantName());
        Log.i("res adapter", restaurant.getImage());
        Glide.with(context).load(restaurant.getImage()).placeholder(R.drawable.foodimg).into(holder.restaurantImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RestaurantActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("restaurant", restaurant);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
        if (restaurant.danhmucList != null) {
            Log.i("RestaurantAdapter", restaurant.getRestaurantName());
            CategoryAdapter2 adapter2 = new CategoryAdapter2(context, restaurant.danhmucList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerViewDanhMuc.setLayoutManager(layoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(holder.recyclerViewDanhMuc.getContext(),
                    ((LinearLayoutManager) layoutManager).getOrientation());
            holder.recyclerViewDanhMuc.addItemDecoration(dividerItemDecoration);
            holder.recyclerViewDanhMuc.setAdapter(adapter2);
        }
    }


    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView restaurantName;
        public ImageView restaurantImage;
        public RecyclerView recyclerViewDanhMuc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.restaurantName);
            restaurantImage = itemView.findViewById(R.id.restaurantImage);
            recyclerViewDanhMuc = itemView.findViewById(R.id.recyclerViewDanhMuc);
        }
    }

}
