package com.example.food_order_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.food_order_demo.Interface.ItemClickListener;
import com.example.food_order_demo.R;
import com.example.food_order_demo.model.Foods;

import java.util.ArrayList;

public class FoodAdapter extends BaseAdapter {
    //public TextView foodName;
    private LinearLayout layout;
    private ItemClickListener itemClickListener;

    ArrayList<Foods> foods = new ArrayList<>();
    Context context;

    public FoodAdapter(Context context, ArrayList<Foods> foods) {
        this.foods = foods;
        this.context = context;
    }

    public int getCount() {
        return foods.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.food_item, parent, false);

        ImageView imgFood = view.findViewById(R.id.FoodImage);
        TextView txtName = view.findViewById(R.id.txtFoodName);
        TextView txtGia = view.findViewById(R.id.txtPrice);
        TextView txtDanhMuc = view.findViewById(R.id.txtDanhMuc);

        Foods food = foods.get(position);

        txtName.setText(food.getFoodName());
        if (food.getPrice() != null)
            txtGia.setText(food.getPrice() + "đ");
        else
            txtGia.setText("0đ");
        txtDanhMuc.setText(food.getCategory());
        Glide.with(context).load(food.getFoodImage()).placeholder(R.drawable.foodimg).into(imgFood);
        return view;
    }
}
