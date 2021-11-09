package com.example.food_order_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_order_demo.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryAdapter2 extends RecyclerView.Adapter<CategoryAdapter2.ViewHolder> {

    Context context;
    List<String> danhmucList;

    public CategoryAdapter2(Context context, List<String> danhmucList) {
        this.context = context;
        this.danhmucList = danhmucList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_danhmuc_nho, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        final String danhmuc = danhmucList.get(position);
        holder.danhmucName.setText(danhmuc);
    }

    @Override
    public int getItemCount() {
        return danhmucList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView danhmucName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            danhmucName = itemView.findViewById(R.id.txtTenDanhMuc);
        }
    }
}
