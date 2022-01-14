package com.example.food_order_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.food_order_demo.R;
import com.example.food_order_demo.common.Common;
import com.example.food_order_demo.model.CartItem;
import com.example.food_order_demo.model.Singleton.Singleton;

public class CartItemInCartAdapter extends BaseAdapter {
    Context context;
    View view;

    public CartItemInCartAdapter(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public int getCount() {
        return Singleton.getInstance().getCurrentCart().getCartItemList().size();
    }

    @Override
    public Object getItem(int position) {
        return Singleton.getInstance().getCurrentCart().getCartItemList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    private class ViewHolder{
        TextView txtTen, txtSL, txtTongTien;
        Button btnSub, btnAdd;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView==null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.dong_giohang,parent,false);

            holder.txtTen = convertView.findViewById(R.id.Giohang_TenMon);
            holder.txtSL = convertView.findViewById(R.id.Giohang_Soluong);
            holder.txtTongTien = convertView.findViewById(R.id.GioHang_ThanhTien);
            holder.btnSub = convertView.findViewById(R.id.buttonSub);
            holder.btnAdd = convertView.findViewById(R.id.buttonAdd);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        CartItem cartItem = Singleton.getInstance().getCurrentCart().getCartItemList().get(position);

        holder.txtTen.setText(cartItem.getFoodName());
        holder.txtSL.setText(cartItem.getSoLuong()+"");
        holder.txtTongTien.setText(Common.printCurrency(Double.parseDouble(cartItem.getPrice())));

        holder.btnSub.setOnClickListener(v -> {
            int sl = Singleton.getInstance().getCurrentCart().getCartItemList().get(position).getSoLuong();
            int oldTotal = Integer.parseInt(Singleton.getInstance().getCurrentCart().getCartItemList().get(position).getPrice());
            int price =  oldTotal/sl;
            sl -= 1;
            if(sl<1) sl = 1;
            int newTotal = price*sl;
            Singleton.getInstance().getCurrentCart().getCartItemList().get(position).setPrice(String.valueOf(newTotal));
            Singleton.getInstance().getCurrentCart().getCartItemList().get(position).setSoLuong(sl);
            int TongTien = Singleton.getInstance().getCurrentCart().getCartTotal();
            Singleton.getInstance().getCurrentCart().setCartTotal(TongTien -oldTotal +newTotal);

            TextView txtTongTien = view.findViewById(R.id.txtTongTien);
            if(txtTongTien !=null)
                txtTongTien.setText("Tổng tiền : " + Singleton.getInstance().getCurrentCart().getCartTotal() +"đ");
            notifyDataSetChanged();
//            holder.txtSL.setText(sl+"");
//            holder.txtTongTien.setText(String.valueOf(price*sl));
        });
        holder.btnAdd.setOnClickListener(v -> {
            int sl = Singleton.getInstance().getCurrentCart().getCartItemList().get(position).getSoLuong();
            int oldTotal = Integer.parseInt(Singleton.getInstance().getCurrentCart().getCartItemList().get(position).getPrice());
            int price =  oldTotal/sl;
            sl += 1;
            if(sl<1) sl = 1;
            int newTotal = price*sl;
            Singleton.getInstance().getCurrentCart().getCartItemList().get(position).setPrice(String.valueOf(newTotal));
            Singleton.getInstance().getCurrentCart().getCartItemList().get(position).setSoLuong(sl);
            int TongTien = Singleton.getInstance().getCurrentCart().getCartTotal();
            Singleton.getInstance().getCurrentCart().setCartTotal(TongTien -oldTotal +newTotal);

            TextView txtTongTien = view.findViewById(R.id.txtTongTien);
            if(txtTongTien !=null)
                txtTongTien.setText("Tổng tiền : " + Singleton.getInstance().getCurrentCart().getCartTotal() +"đ");

            notifyDataSetChanged();
//            holder.txtSL.setText(sl+"");
//            holder.txtTongTien.setText(String.valueOf(price*sl));
        });


        return convertView;
    }
}
