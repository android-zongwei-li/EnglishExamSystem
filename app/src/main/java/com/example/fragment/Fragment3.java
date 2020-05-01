package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.activity.activity_in_fragment3.LoginActivity;
import com.example.myapplication.R;

import static android.app.Activity.RESULT_OK;

public class Fragment3 extends Fragment {

    LinearLayout ll_login;      // 登录控件

    TextView tv_telephone;

    private final static int REQUEST_PHONE = 1; // 返回手机号

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_3,null);

        ll_login = view.findViewById(R.id.ll_login);
        ll_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(getContext(), LoginActivity.class),REQUEST_PHONE);

            }
        });

        tv_telephone = view.findViewById(R.id.tv_telephone);

        //收藏
        Button btnCollection = view.findViewById(R.id.btn_collection);
        btnCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CollectionActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PHONE) {
                tv_telephone.setText(data.getExtras().getString(LoginActivity.PARAM_PHONE));
            }
        }
    }
}