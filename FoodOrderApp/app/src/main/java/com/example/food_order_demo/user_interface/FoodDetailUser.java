package com.example.food_order_demo.user_interface;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.food_order_demo.R;
import com.example.food_order_demo.model.Cart;
import com.example.food_order_demo.model.CartItem;
import com.example.food_order_demo.model.Foods;
import com.example.food_order_demo.model.Singleton.Singleton;

import java.util.List;

public class FoodDetailUser extends AppCompatActivity {

    String restaurantID;
    String restaurantName;
    Foods food;
    TextView txtTenMonAN, txtGia, txtMoTa, txtDanhMuc;
    ImageView imgfood;
    Button btnBack, btnAddToCart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_chitietmonan);

        Toolbar toolbar =  findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            food = (Foods) b.getSerializable("Food");
            restaurantID = b.getString("restaurantID");
            restaurantName = b.getString("restaurantName");
        }

        mapping();

        if(food!=null)
        {
            txtTenMonAN.setText(food.getFoodName());
            txtGia.setText(food.getPrice()+"đ");
            txtDanhMuc.setText(food.getCategory());
            txtMoTa.setText(food.getDescription());
            Glide.with(this).load(food.getFoodImage()).placeholder(R.drawable.foodimg).into(imgfood);
            Log.i("FoodDetailLog",food.getFoodName());
        }

        btnBack.setOnClickListener(v -> {
            finish();
        });
        btnAddToCart.setOnClickListener(v -> {
            showDialogAddToCart(food);
        });

    }

    private void showDialogAddToCart(Foods food) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_addtocart);
            //mapping
            TextView txtSoLuong = dialog.findViewById(R.id.txtSoLuong);
            Button btnSub = dialog.findViewById(R.id.btnSub);
            Button btnAdd = dialog.findViewById(R.id.btnAdd);
            Button btnAddToCart = (Button) dialog.findViewById(R.id.btnXacNhan);
            Button btnCancel = (Button) dialog.findViewById(R.id.btnHuy);

            btnSub.setOnClickListener(v -> {
                int sl = Integer.parseInt(txtSoLuong.getText().toString().trim());
                sl = sl-1;
                if(sl<1) sl =1;
                txtSoLuong.setText(sl+"");
            });
            btnAdd.setOnClickListener(v -> {
                int sl = Integer.parseInt(txtSoLuong.getText().toString().trim());
                sl = sl+1;
                if(sl<1) sl =1;
                txtSoLuong.setText(sl+"");
            });
            btnAddToCart.setOnClickListener(v -> {
                if(Singleton.getInstance().getCurrentCart().getRestaurantID().equals(restaurantID)
                    || Singleton.getInstance().getCurrentCart().getRestaurantID().equals("")) {
                    AddToCart(Integer.parseInt(txtSoLuong.getText().toString()));


                }
                else{
                    Log.i("AddToCartDialog","Bạn chưa hoàn thành giỏ hàng của nhà hàng khác");
                    Toast.makeText(this,"Bạn chưa hoàn thành giỏ hàng của nhà hàng khác",Toast.LENGTH_SHORT).show();
                }


                dialog.dismiss();
            });
            btnCancel.setOnClickListener(v -> dialog.dismiss());
            dialog.show();

    }

    private void AddToCart(int sl) {
        Cart cart = Singleton.getInstance().getCurrentCart();
        List<CartItem> cartItemList =  cart.getCartItemList();
        CartItem cartItem;
        int total = cart.getCartTotal();

        cart.setRestaurantID(restaurantID);
        cart.setRestaurantName(restaurantName);
        cartItem = food.toCartItem(sl);
        cartItemList.add(cartItem);
        total+= Integer.parseInt(food.getPrice())*sl;

        cart.setCartItemList(cartItemList);
        cart.setCartTotal(total);
        Singleton.getInstance().setCurrentCart(cart);

        Singleton.getInstance().getCurrentCart().setRestaurantID(restaurantID);
        Log.i("AddToCartDialog", food.getFoodName() + " số lượng " + sl);
        Toast.makeText(this,"Đã thêm vào giỏ hàng",Toast.LENGTH_SHORT).show();
        finish();

    }

    private void mapping() {
        txtTenMonAN = findViewById(R.id.txtName);
        txtMoTa = findViewById(R.id.txtMoTa);
        txtGia = findViewById(R.id.txtGiaBan);
        txtDanhMuc = findViewById(R.id.txtDanhMuc);
        btnBack = findViewById(R.id.btnBack);
        btnAddToCart = findViewById(R.id.btnThemVaoGioHang);
        imgfood = findViewById(R.id.imgFood);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
