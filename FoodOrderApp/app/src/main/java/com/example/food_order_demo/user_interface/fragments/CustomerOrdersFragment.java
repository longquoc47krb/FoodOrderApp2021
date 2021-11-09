package com.example.food_order_demo.user_interface.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.food_order_demo.R;
import com.example.food_order_demo.adapter.RequestAdapter;
import com.example.food_order_demo.model.Builder.Request;
import com.example.food_order_demo.model.Singleton.Singleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class CustomerOrdersFragment extends Fragment {

    ListView listView;
    DatabaseReference reference;
    List<Request> requestList;
    RequestAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.customer_fragment_orders, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);

        requestList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Request");
        adapter = new RequestAdapter(requestList,R.layout.request_item,getContext());
        listView.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                requestList.clear();
                for(DataSnapshot child : snapshot.getChildren()){
                    Request request = child.getValue(Request.class);
                    request.setRequestID(child.getKey());
                    if(request.getCustomerID().equals(Singleton.getInstance().getUserID()))
                    {
                        requestList.add(request);
                        Log.d("CustomerOrder", requestList.size()+"");
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void mapping(View view) {
        listView = view.findViewById(R.id.lvDonHangCustomer);
    }
}
