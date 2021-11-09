package com.example.food_order_demo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.food_order_demo.model.Restaurant;
import com.example.food_order_demo.model.Singleton.Singleton;
import com.example.food_order_demo.quanlydanhmuc.CategoryFragment;
import com.example.food_order_demo.quanlydonhang.QuanLyDonHangFragment;
import com.example.food_order_demo.quanlythongtin.ProfileFragment;
import com.example.food_order_demo.quanlythucdon.FoodsFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String SELECTED_ITEM_ID = "SELECTED_ITEM_ID";
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private long backPressedTime;
    private Toast backToast;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    //DatabaseReference table_restaurant;
    ImageView resImage;
    TextView resName;
    String title;
    String Uid;
    Fragment fragment = null;
    private String DEFAULT_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/food-order-app-hcmute.appspot.com/o/profile-anonymous-face-icon-gray-silhouette-person-male-default-avatar-photo-placeholder-isolated-white-background-profile-107327860.jpg?alt=media&token=07fce3de-7f4f-4d0f-96a6-6e7f6f83f72b";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);
        // mapping

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        Uid = mAuth.getCurrentUser().getUid().toString();
        Log.i("Testing",Uid);
        Singleton.getInstance().setRestaurantRef(database.getReference("Restaurants").child(Uid));
        mToolbar = (Toolbar) findViewById(R.id.admin_toolbar_main);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Trang chủ");
       showHome();
    }

    @Override
    protected void onStart() {
        super.onStart();
        defineNavigationView();
    }

    private void defineNavigationView() {
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = mNavigationView.getHeaderView(0);
        resName = view.findViewById(R.id.header_RestaurantName);
        resImage = view.findViewById(R.id.header_RestaurantImage);
        resImage.setImageResource(R.drawable.profile);
        // navigation draw - thanh menubar
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
        getUserProfileData();
    }
    private void getUserProfileData(){

        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Restaurant restaurant = snapshot.getValue(Restaurant.class);
                    String Image = snapshot.child("image").getValue().toString();
                    resName.setText(restaurant.getRestaurantName());

                    if (Image.equals("default")) {
                        Picasso.get().load(R.drawable.profile).into(resImage);
                    } else
                        Picasso.get().load(Image).placeholder(R.drawable.profile).into(resImage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        Singleton.getInstance().getRestaurantRef().addListenerForSingleValueEvent(valueEventListener);

    }
    private void showHome() {
        getSupportActionBar().setTitle("Trang chủ");
        fragment = new AdminHomeFragment();
        if (fragment != null){
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.mainLayout, fragment, fragment.getTag()).commit();
        }
    }
    private void showDonHang() {
        getSupportActionBar().setTitle(R.string.menu_orders);
        fragment = new QuanLyDonHangFragment();
        if (fragment != null){
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.mainLayout, fragment, fragment.getTag()).commit();
        }
    }
    private void showFoods() {
        getSupportActionBar().setTitle(R.string.menu_foods);
        fragment = new FoodsFragment();
        if (fragment != null){
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.mainLayout, fragment, fragment.getTag()).commit();
        }
    }
    private void showCategoryFragment(){
        getSupportActionBar().setTitle(R.string.menu_categories);
        fragment = new CategoryFragment();
        if (fragment != null){
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.mainLayout, fragment, fragment.getTag()).commit();
        }
    }
    private void showProfileManagement() {
        getSupportActionBar().setTitle(R.string.menu_chinhsuathongtin);
        fragment = new ProfileFragment();
        if (fragment != null){
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.mainLayout, fragment, fragment.getTag()).commit();
        }
    }
    private void changePassword() {
        //getSupportActionBar().setTitle(R.string.menu_doimatkhau);
//        fragment = new ChangePasswordFragment();
//        if (fragment != null){
//            FragmentManager manager = getSupportFragmentManager();
//            manager.beginTransaction().replace(R.id.mainLayout, fragment, fragment.getTag()).commit();
//        }
        Intent intent = new Intent(AdminMain.this,ChangePassword.class);
        startActivity(intent);
    }

    @SuppressLint("ShowToast")
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragment instanceof AdminHomeFragment){
                if (backPressedTime + 2000 > System.currentTimeMillis())
                {
                    super.onBackPressed();
                    return;
                }
                else {
                    showHome();
                    backToast = Toast.makeText(getBaseContext(), "Press back again to Exit", Toast.LENGTH_SHORT);
                    backToast.show();
                }
                backPressedTime  = System.currentTimeMillis();
            }
            else if (fragment instanceof CategoryFragment){
                if (backPressedTime + 1000 > System.currentTimeMillis())
                {
                    showHome();
                }
                else {
                    showCategoryFragment();
                    backToast = Toast.makeText(getBaseContext(), "Press back again to Home", Toast.LENGTH_SHORT);
                    backToast.show();
                }
                backPressedTime  = System.currentTimeMillis();
            }
            /*else if (fragment instanceof FaqFragment){
                if (backPressedTime + 1000 > System.currentTimeMillis())
                {
                    backToast.cancel();
                    showHome();
                }
                else {
                    showFaq();
                    backToast = Toast.makeText(getBaseContext(), "Press back again to Home", Toast.LENGTH_SHORT);
                    backToast.show();
                }
                backPressedTime  = System.currentTimeMillis();
            }
            else if (fragment instanceof ContactUsFragment){
                if (backPressedTime + 1000 > System.currentTimeMillis())
                {
                    backToast.cancel();
                    showHome();
                }
                else {
                    showContactUs();
                    backToast = Toast.makeText(getBaseContext(), "Press back again to Home", Toast.LENGTH_SHORT);
                    backToast.show();
                }
                backPressedTime  = System.currentTimeMillis();
            }
            else if (fragment instanceof ServiceAreasFragment){
                if (backPressedTime + 1000 > System.currentTimeMillis())
                {
                    backToast.cancel();
                    showHome();
                }
                else {
                    showServiceAreas();
                    backToast = Toast.makeText(getBaseContext(), "Press back again to Home", Toast.LENGTH_SHORT);
                    backToast.show();
                }
                backPressedTime  = System.currentTimeMillis();
            }
            else if (fragment instanceof WalletFragment){
                if (backPressedTime + 1000 > System.currentTimeMillis())
                {
                    backToast.cancel();
                    showHome();
                }
                else {
                    showWallet();
                    backToast = Toast.makeText(getBaseContext(), "Press back again to Home", Toast.LENGTH_SHORT);
                    backToast.show();
                }
                backPressedTime  = System.currentTimeMillis();
            }
            else if (fragment instanceof SettingFragment){
                if (backPressedTime + 1000 > System.currentTimeMillis())
                {
                    backToast.cancel();
                    showHome();
                }
                else {
                    showSetting();
                    backToast = Toast.makeText(getBaseContext(), "Press back again to Home", Toast.LENGTH_SHORT);
                    backToast.show();
                }
                backPressedTime  = System.currentTimeMillis();
            }*/
            else {
                showHome();
            }
        }
    }

//    private void loadRestaurantInfo() {
//        //init firebase
//        Singleton.getInstance().getRestaurantRef().child(Uid)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.exists()){
//                            Restaurant res = snapshot.getValue(Restaurant.class);
//                            title = res.getRestaurantName();
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_home:
                showHome();
                break;
            case R.id.nav_quanlydanhmuc:
                showCategoryFragment();
                break;
            case R.id.nav_quanlydonhang:
                showDonHang();
                break;
            case R.id.nav_quanlymonan:
                showFoods();
                break;
            case R.id.nav_chinhsuathongtin:
                showProfileManagement();
                break;
            case R.id.nav_doimatkhau:
                changePassword();
                break;
            case R.id.nav_dangxuat:
                showDialog();
                break;
            default:
                showHome();
                break;
        }



        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    // Hiện dialog đăng xuất
    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dangxuat);
        //mapping
        Button btnSignOut = (Button) dialog.findViewById(R.id.btnXacNhanDX);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnHuyDX);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(AdminMain.this, DangNhap.class));
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}
