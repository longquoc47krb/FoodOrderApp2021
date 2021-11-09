package com.example.food_order_demo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SanPhamTrongDonHangAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<SanPhamTrongDonHang> sanphamList;

    public SanPhamTrongDonHangAdapter(Context context, int layout, List<SanPhamTrongDonHang> sanphamList) {
        this.context = context;
        this.layout = layout;
        this.sanphamList = sanphamList;
    }

    @Override
    public int getCount() {
        return sanphamList.size();
    }

    @Override
    public Object getItem(int position) {
        return sanphamList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout,null);

        TextView txtTenMonAn = convertView.findViewById(R.id.txtTenMonAn_ChitietDonHang);
        TextView txtSoLuong = convertView.findViewById(R.id.txtSoLuong_ChitietDonHang);
        TextView txtGia = convertView.findViewById(R.id.txtGia_ChitietDonHang);

        SanPhamTrongDonHang sanPham = sanphamList.get(position);
        txtTenMonAn.setText(sanPham.tenSanPham);
        txtSoLuong.setText(sanPham.soLuong+"x");
        txtGia.setText(sanPham.Gia +"đ");

        return convertView;
    }
}
