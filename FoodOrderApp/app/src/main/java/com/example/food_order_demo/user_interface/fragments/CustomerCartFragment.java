package com.example.food_order_demo.user_interface.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.food_order_demo.R;
import com.example.food_order_demo.adapter.CartItemInCartAdapter;
import com.example.food_order_demo.model.Builder.IRequestBuilder;
import com.example.food_order_demo.model.Builder.NewRequestBuilder;
import com.example.food_order_demo.model.Builder.Request;
import com.example.food_order_demo.model.Cart;
import com.example.food_order_demo.model.CartItem;
import com.example.food_order_demo.model.Singleton.Singleton;
import com.example.food_order_demo.quanlythucdon.FoodsFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CustomerCartFragment extends Fragment {

    TextView txtTen, txtSDT, txtDiaChi, txtNhaHang;
    Button btnXacNhanGioHang, btnXoaGioHang;
    ListView lvCartItem;
    TextView txtTongTien;
    CartItemInCartAdapter adapter;
    String name,sdt,diachi,nhahang,tongtien;
    DatabaseReference db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.customer_fragment_cart, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);




//        CurrentAccount.getInstance().getCartItemList().add(new CartItem("test","test","20000",1));
//        CurrentAccount.getInstance().setCartTotal(20000);

        name = Singleton.getInstance().getUser().getFullname();
        diachi = Singleton.getInstance().getUser().getAddress();
        nhahang = Singleton.getInstance().getCurrentCart().getRestaurantName();
        sdt = Singleton.getInstance().getUser().getPhone();
        tongtien = String.valueOf(Singleton.getInstance().getCurrentCart().getCartTotal());

        txtTen.setText(name);
        txtDiaChi.setText(diachi);
        txtSDT.setText(sdt);
        txtTongTien.setText("Tổng tiền : "+ tongtien+"đ");
        txtNhaHang.setText(nhahang);

        TextView empty = view.findViewById(R.id.emptyText);
        adapter = new CartItemInCartAdapter(getContext(),getView());
        lvCartItem.setAdapter(adapter);
        lvCartItem.setEmptyView(empty);

        if(Singleton.getInstance().getCurrentCart().getRestaurantID().equals("") || nhahang.isEmpty())
            btnXacNhanGioHang.setEnabled(false);

        btnXacNhanGioHang.setOnClickListener(v -> {
            UploadRequestToFirebase();
        });

        btnXoaGioHang.setOnClickListener(v -> {
            Singleton.getInstance().setCurrentCart(new Cart());
            goToHomeFragment();
        });
    }

    private void UploadRequestToFirebase() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        sdt = txtSDT.getText().toString().trim();
        diachi = txtDiaChi.getText().toString();

        IRequestBuilder builder = new NewRequestBuilder()
                                        .setRestaurantID(Singleton.getInstance().getCurrentCart().getRestaurantID())
                                        .setCustomerID(Singleton.getInstance().getUserID())
                                        .setName(name)
                                        .setAddress(diachi)
                                        .setPhone(sdt)
                                        .setStatus("0")
                                        .setTotal(tongtien)
                                        .setTime(date)
                                        .setRequestID(System.currentTimeMillis()+"-"
                                                +Singleton.getInstance().getCurrentCart().getRestaurantID());
        Request request = builder.build();

        db = FirebaseDatabase.getInstance().getReference("Request");
        db.child(request.getRequestID()).setValue(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                UoloadFoodToRequest(request);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getContext(),"Đặt hàng thất bại!\nXin hãy kiểm tra lại kết nối internet của bạn",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UoloadFoodToRequest(Request request) {
        for(CartItem item:Singleton.getInstance().getCurrentCart().getCartItemList())
        {
            db.child(request.getRequestID()).child("foods").child(item.getFoodName() +"-"+ item.getFoodID())
                    .setValue(item)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Singleton.getInstance().setCurrentCart(new Cart());
                            Toast.makeText(getContext(),"Đặt hàng thành công",Toast.LENGTH_SHORT).show();
                            goToHomeFragment();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(getContext(),"Đặt hàng bị lỗi!",Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    private void goToHomeFragment(){
        getFragmentManager().beginTransaction().replace(R.id.frame_content,new CustomerHomeFragment()).commit();
    }

    private void mapping(View v) {
        txtTen = v.findViewById(R.id.edtTenKH_Giohang);
        txtNhaHang = v.findViewById(R.id.edtTenNH_Giohang);
        txtDiaChi = v.findViewById(R.id.edtDiaChi_giohang);
        txtSDT = v.findViewById(R.id.edtSDT_Giohang);
        btnXacNhanGioHang = v.findViewById(R.id.buttonXacNhanDonHang);
        btnXoaGioHang = v.findViewById(R.id.buttonXoaGioHang);
        lvCartItem = v.findViewById(R.id.listGioHang);
        txtTongTien = v.findViewById(R.id.txtTongTien);
    }
}
