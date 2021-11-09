package com.example.food_order_demo.quanlythucdon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.food_order_demo.quanlythucdon.quanlymon;
import com.example.food_order_demo.R;

import java.util.List;

public class quanlymonAdapter extends BaseAdapter{
    private Context context;
    private int Layout;
    private List<quanlymon> monanList;

    public quanlymonAdapter(Context context, int layout, List<quanlymon> monanList) {
        this.context = context;
        Layout = layout;
        this.monanList = monanList;
    }

    @Override
    public int getCount() {
        return monanList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class ViewHorder{
        TextView Ten;
        ImageView imghinh;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHorder viewHorder;
        if(convertView==null)
        {
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(Layout,null);
            viewHorder=new ViewHorder();
            viewHorder.Ten=convertView.findViewById(R.id.txttenmon);
            viewHorder.imghinh=convertView.findViewById(R.id.imghinh);
            convertView.setTag(viewHorder);
        }
        else{
            viewHorder= (ViewHorder) convertView.getTag();
        }


        quanlymon quanlymon= monanList.get(position);
        viewHorder.Ten.setText(quanlymon.getTen());
        viewHorder.imghinh.setImageResource(quanlymon.getHinh());
        return convertView;
    }
}
