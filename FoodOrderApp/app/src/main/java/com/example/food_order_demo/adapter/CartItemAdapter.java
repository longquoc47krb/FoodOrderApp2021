package com.example.food_order_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.food_order_demo.R;
import com.example.food_order_demo.model.CartItem;

import java.util.List;

public class CartItemAdapter extends BaseAdapter {

    Context context;
    List<CartItem> cartItemList;

    public CartItemAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @Override
    public int getCount() {
        return cartItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtSL ;
        TextView txtTen ;
        TextView txtGia ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null)
        {
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.dong_donhang,parent,false);

            holder = new ViewHolder();

            holder.txtSL = convertView.findViewById(R.id.txtSoLuong_DonHang);
            holder.txtTen = convertView.findViewById(R.id.txtTenMonAn_DonHang);
            holder.txtGia = convertView.findViewById(R.id.txtGia_DonHang);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }


        CartItem food = cartItemList.get(position);

        holder.txtSL.setText(food.getSoLuong()+"");
        holder.txtTen.setText(food.getFoodName());
        holder.txtGia.setText(food.getPrice());

        return convertView;
    }
}
