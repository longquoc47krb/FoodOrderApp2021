package com.example.food_order_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.food_order_demo.R;
import com.example.food_order_demo.model.Builder.Request;

import java.util.List;

public class RequestAdapter extends BaseAdapter {

    private List<Request> requestList;
    private int layout;
    private Context context;

    public RequestAdapter(List<Request> requestList, int layout, Context context) {
        this.requestList = requestList;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public int getCount() {
        return requestList.size();
    }

    @Override
    public Object getItem(int position) {
        return requestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout,null);

        TextView txtTime = convertView.findViewById(R.id.txtTimeDH);
        TextView txtTenDH = convertView.findViewById(R.id.txtTenDH);
        TextView txtStatusDH = convertView.findViewById(R.id.txtStatusDH);


        //gán
        Request request = requestList.get(position);
        txtTime.setText(request.getTime().toString());
        txtTenDH.setText(request.getRequestID());
        switch (request.getStatus())
        {
            case "1": txtStatusDH.setText("Đã đặt"); break;
            case "2": txtStatusDH.setText("Đang vận chuyển"); break;
            case "3": txtStatusDH.setText("Đã chuyển"); break;
            case "0": txtStatusDH.setText("Chờ xác nhận"); break;
            // Mặc định là 0, 0:Đã đặt, 1:Đang vận chuyển, 2:Đã chuyển, 3:Hủy

        }

        return convertView;
    }
}
