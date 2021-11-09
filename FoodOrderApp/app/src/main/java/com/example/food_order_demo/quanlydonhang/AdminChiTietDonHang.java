package com.example.food_order_demo.quanlydonhang;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.food_order_demo.R;
import com.example.food_order_demo.adapter.CartItemAdapter;
import com.example.food_order_demo.model.CartItem;
import com.example.food_order_demo.model.Builder.Request;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminChiTietDonHang extends AppCompatActivity {

    TextView txtName, txtSdt, txtDiachi;
    ListView listView;
    TextView txtTongTien;
    Spinner spinner;
    Request currentReq;
    ArrayList<CartItem> foodsList;
    DatabaseReference reference;
    DatabaseReference currentReqRef;
    CartItemAdapter adapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_chitietdonhang);

        Intent intent = new Intent();
        intent.putExtra("editTextValue", "value_here");
        setResult(RESULT_OK, intent);

        Toolbar toolbar =  findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mapping();

        foodsList = new ArrayList<>();
        adapter = new CartItemAdapter(this,foodsList);
        listView.setAdapter(adapter);
        getFoodsListFromRequest();


        txtName.setText( currentReq.getName());
        txtSdt.setText(currentReq.getPhone());
        txtDiachi.setText( currentReq.getAddress());
        txtTongTien.setText("Tổng tiền: " + currentReq.getTotal());
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.trangthaiDH, R.layout.spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        setSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position != Integer.parseInt(currentReq.getStatus()))
                    showDialogChuyenTrangThai(AdminChiTietDonHang.this,"Chỉnh sửa trạng thái đơn hàng","Bạn có chắc chẵn không?",position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }



    public void showDialogChuyenTrangThai(Activity activity, String title, CharSequence message, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (title != null) builder.setTitle(title);

        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentReqRef.child("status").setValue(position+"").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        currentReq.setStatus(position+"");
                        dialog.dismiss();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void getFoodsListFromRequest() {
       reference = FirebaseDatabase.getInstance().getReference("Request");
       currentReqRef = reference.child(currentReq.getRequestID());

        Query checkRequests = currentReqRef.child("foods");
        checkRequests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    CartItem food = dataSnapshot.getValue(CartItem.class);
                    assert food != null;
                    Log.i("CTDH cartitem",food.getFoodID());
                    foodsList.add(food);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setSpinner() {
        switch (currentReq.getStatus()){
            case "0": spinner.setSelection(0); break;
            case "1": spinner.setSelection(1); break;
            case "2": spinner.setSelection(2); break;
            case "3": spinner.setSelection(3); break;
            default: spinner.setSelection(0); break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
    private void mapping() {
        txtName = findViewById(R.id.txtTenKH);
        txtSdt = findViewById(R.id.txtSDT);
        txtDiachi = findViewById(R.id.txtDiaChi);
        listView = findViewById(R.id.listCTDH);
        txtTongTien = findViewById(R.id.txtTongTien);
        spinner = findViewById(R.id.spinner);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            currentReq = (Request) b.getSerializable("request");
            Log.i("CTDH",currentReq.getRequestID());
        }
    }
}
