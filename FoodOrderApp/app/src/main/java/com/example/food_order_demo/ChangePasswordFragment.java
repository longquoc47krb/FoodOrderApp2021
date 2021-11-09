package com.example.food_order_demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.food_order_demo.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String Uid;
    FirebaseAuth mAuth;
    DatabaseReference table_user;
    EditText edtOldPassword, edtNewPassword, edtConfirmPW;
    Button btnDoiMatKhau;
    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public void mapping(View view){
        edtOldPassword = view.findViewById(R.id.old_pasword);
        edtNewPassword = view.findViewById(R.id.new_password);
        edtConfirmPW = view.findViewById(R.id.confirm_password);
        btnDoiMatKhau = view.findViewById(R.id.changePass);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        btnDoiMatKhau.setOnClickListener(this);
    }

    private void changePassword() {
        mAuth = FirebaseAuth.getInstance();
        Uid = mAuth.getCurrentUser().getUid();
        String oldpass = edtOldPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirm = edtConfirmPW.getText().toString().trim();
        if(oldpass.isEmpty()){
            Toast.makeText(getActivity(), "Vui lòng nhập mật khẩu cũ", Toast.LENGTH_SHORT).show();
            edtOldPassword.setFocusable(View.FOCUSABLE_AUTO);
        }
        else if(newPassword.isEmpty()){
            Toast.makeText(getActivity(), "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();

            edtNewPassword.setFocusable(View.FOCUSABLE_AUTO);
        }
        else if(confirm.equals(oldpass)){
            Toast.makeText(getActivity(), "Mật khẩu mới và mật khẩu cũ không được trùng nhau.", Toast.LENGTH_SHORT).show();
            edtConfirmPW.setFocusable(View.FOCUSABLE_AUTO);
        }
        else if(confirm.isEmpty()){
            Toast.makeText(getActivity(), "Vui lòng xác nhận mật khẩu mới.", Toast.LENGTH_SHORT).show();
            edtConfirmPW.setFocusable(View.FOCUSABLE_AUTO);
        }
        else{
            table_user = FirebaseDatabase.getInstance().getReference("User").child(Uid);
            FirebaseUser user;
            user = mAuth.getCurrentUser();
            final String email = user.getEmail();
            AuthCredential credential = EmailAuthProvider.getCredential(email,oldpass);

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getActivity(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                    checkUserPermission();
                                }else {
                                    Toast.makeText(getActivity(), "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }else {
                        Toast.makeText(getActivity(), "Thất bại", Toast.LENGTH_SHORT).show();

                    }
                }
            });




        }



    }
    private void checkUserPermission(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference user = database.getReference("User").child(mAuth.getCurrentUser().getUid());
        ValueEventListener listener = new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user.getRole().equals("admin")){
                    startActivity(new Intent(getActivity(), AdminMain.class));
                }
                else{
//                   startActivity(new Intent(DangNhap.this, CustomerHome.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        user.addValueEventListener(listener);
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
        return inflater.inflate(R.layout.doimatkhau, container, false);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.changePass){
            changePassword();
        }

    }
}