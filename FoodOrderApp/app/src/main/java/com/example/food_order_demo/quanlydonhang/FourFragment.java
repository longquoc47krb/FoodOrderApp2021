package com.example.food_order_demo.quanlydonhang;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.food_order_demo.R;
import com.example.food_order_demo.adapter.RequestAdapter;
import com.example.food_order_demo.model.Builder.Request;
import com.example.food_order_demo.model.Singleton.Singleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FourFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FourFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REFRESH = 999;
    private static final int RESULT_OK = -1;
    DatabaseReference reference;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Request> requestList;
    private ListView listView;
    private RequestAdapter adapter;

    SwipeRefreshLayout refreshLayout;

    public FourFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FourFragment newInstance(String param1, String param2) {
        FourFragment fragment = new FourFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_four, container, false);
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);

        refreshLayout.setOnRefreshListener(this::onRefresh);
        requestList = new ArrayList<>();
        //requestList.add(new Request("test","test","test","0","3",null, "28/5/2021","",""));
        reference = FirebaseDatabase.getInstance().getReference("Request");
        Query checkRequests = reference.orderByChild("status").equalTo("3");
        adapter = new RequestAdapter(requestList,R.layout.request_item,getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Intent intent = new Intent(getContext(),AdminChiTietDonHang.class);
            Bundle b = new Bundle();
            b.putSerializable("request",requestList.get(position));
            intent.putExtras(b);
            startActivityForResult(intent,REFRESH);
        });

        checkRequests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    Request request = dataSnapshot.getValue(Request.class);
                    request.setRequestID(dataSnapshot.getKey());
                    Log.d("TEST req4 rID", request.getRestaurantID());
                    Log.d("TEST req4 cID", Singleton.getInstance().getUserID());
                    if(request.getRestaurantID().equals(Singleton.getInstance().getUserID()))
                        requestList.add(request);
                    Log.d("TEST req4 list size",requestList.size()+"");
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REFRESH && resultCode == RESULT_OK)
        {
            reference = FirebaseDatabase.getInstance().getReference("Request");
            Query checkRequests = reference.orderByChild("status").equalTo("3");
            checkRequests.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        requestList.clear();
                        Request request = dataSnapshot.getValue(Request.class);
                        Log.d("TEST req rID", request.getRestaurantID());
                        Log.d("TEST req cID", Singleton.getInstance().getUserID());
                        if(request.getRestaurantID().equals(Singleton.getInstance().getUserID()))
                            requestList.add(request);
                        Log.d("TEST req list size",requestList.size()+"");
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void mapping(View view)
    {
        refreshLayout = view.findViewById(R.id.refresh4);
        listView = view.findViewById(R.id.lvDonHangFour);
    }

    @Override
    public void onRefresh() {
        reference = FirebaseDatabase.getInstance().getReference("Request");
        Query checkRequests = reference.orderByChild("status").equalTo("3");
        checkRequests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    requestList.clear();
                    Request request = dataSnapshot.getValue(Request.class);
                    Log.d("TEST req rID", request.getRestaurantID());
                    Log.d("TEST req cID", Singleton.getInstance().getUserID());
                    if(request.getRestaurantID().equals(Singleton.getInstance().getUserID()))
                        requestList.add(request);
                    Log.d("TEST req list size",requestList.size()+"");
                }
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}