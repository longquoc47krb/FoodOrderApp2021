package com.example.food_order_demo.user_interface;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.food_order_demo.AdminMain;
import com.example.food_order_demo.ChangePassword;
import com.example.food_order_demo.DangNhap;
import com.example.food_order_demo.R;
import com.example.food_order_demo.user_interface.fragments.CustomerCartFragment;
import com.example.food_order_demo.user_interface.fragments.CustomerHomeFragment;
import com.example.food_order_demo.user_interface.fragments.CustomerOrdersFragment;
import com.example.food_order_demo.user_interface.fragments.CustomerProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;


public class CustomerMain extends AppCompatActivity implements View.OnClickListener {

    private BottomNavigationView mBottomNav;
    public int mPosition;
    private static final String TAG_HOME = "home";
    private static final String TAG_CART = "cart";
    private static final String TAG_ORDERS = "orders";
    private static final String TAG_PROFILE = "profile";
    public static String CURRENT_TAG = TAG_HOME;

    private int clickItem;


    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_main);

        mBottomNav = findViewById(R.id.bottom_navigation);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.customerDrawer);

        setupBottomNavigation();
        setupNavigationView();

        //position navigation bottom
        mPosition = 0;
        if(savedInstanceState != null){
            mPosition = savedInstanceState.getInt("position", 0);
        }
        setFragment(getFragment(mPosition));
    }

    private void setupNavigationView() {
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home: setFragment(getFragment(0)); mDrawerLayout.closeDrawers(); break;
                    case R.id.nav_dangxuat:  mDrawerLayout.closeDrawers(); showDialog();  break;
                    case R.id.nav_doimatkhau: showChangePassword(); mDrawerLayout.closeDrawers(); break;
                    default: setFragment(getFragment(0)); mDrawerLayout.closeDrawers(); break;
                }
                return true;
            }
        });
    }

    private void showChangePassword() {
        Intent intent = new Intent(CustomerMain.this, ChangePassword.class);
        startActivity(intent);
    }

    private void setFragment(Fragment fragment){
        mBottomNav.getMenu().getItem(mPosition).setChecked(true);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_content, fragment, CURRENT_TAG);
        try {
            ft.commit();
        }catch (Exception e){
            ft.commitAllowingStateLoss();
        }

        Menu menu = mBottomNav.getMenu();
        menu.getItem(mPosition).setChecked(true);
    }

    private Fragment getFragment(int position){
        CURRENT_TAG = TAG_HOME;
        switch (position){
            case 0:
                CURRENT_TAG = TAG_HOME;
                return new CustomerHomeFragment();
            case 3:
                CURRENT_TAG = TAG_PROFILE;
                return new CustomerProfileFragment();
            case 1:
                CURRENT_TAG = TAG_ORDERS;
                return new CustomerOrdersFragment();
            case 2:
                CURRENT_TAG = TAG_CART;
                return new CustomerCartFragment();
        }
        return new CustomerHomeFragment();
    }

    public void selectPosition(int position){
        mPosition = position;
        setFragment(getFragment(position));
        mBottomNav.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putInt("position", mPosition);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void setupBottomNavigation() {
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nv_home:
                        mPosition = 0;
                        break;
                    case R.id.nv_orders:
                        mPosition = 1;
                        break;
                    case R.id.nv_cart:
                        mPosition = 2;
                        break;
                    case R.id.nv_profile:
                        mPosition = 3;
                        break;
                }
                selectPosition(mPosition);
                item.setChecked(true);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else if(CURRENT_TAG != TAG_HOME){
            mPosition = 0;
            selectPosition(mPosition);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dangxuat);
        //mapping
        Button btnSignOut = (Button) dialog.findViewById(R.id.btnXacNhanDX);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnHuyDX);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                startActivity(new Intent(CustomerMain.this, DangNhap.class));
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