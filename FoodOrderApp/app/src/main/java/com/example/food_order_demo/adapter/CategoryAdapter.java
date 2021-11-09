package com.example.food_order_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.food_order_demo.Interface.ItemClickListener;
import com.example.food_order_demo.R;
import com.example.food_order_demo.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter
{

    public TextView categoryName;
    private LinearLayout layout;
    private ItemClickListener itemClickListener;

    ArrayList<Category> categories  = new ArrayList<>();
    Context context;

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.categories = categories;
        this.context = context;

    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.category_singlerow,parent,false);

        TextView category_Name= (TextView) view.findViewById(R.id.tenDanhMuc);

        Category category= categories.get(position);

        category_Name.setText(category.getCategoryName());
        return view;
    }

}
