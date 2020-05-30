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

import com.example.MyApplication;
import com.example.activity.activity_in_fragment3.AboutMeActivity;
import com.example.activity.activity_in_fragment3.CollectionActivity;
import com.example.activity.activity_in_fragment3.LoginActivity;
import com.example.activity.activity_in_fragment3.SettingActivity;
import com.example.myapplication.R;
import com.example.utils.AccountManager;
import com.example.utils.LogUtils;
import com.example.utils.ToastUtils;

import static android.app.Activity.RESULT_OK;

public class Fragment3 extends Fragment {

    private LinearLayout ll_login;      // 登录控件
    private TextView tv_telephone;
    private TextView tv_login_tips;

    private final static int REQUEST_PHONE = 1; // 返回手机号

    private final static int REQUEST_LOGOUT_RESULT = 3;

    private AccountManager am;
    private String telephone;

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

        tv_login_tips = view.findViewById(R.id.tv_login_tips);

        am = AccountManager.getInstance(getActivity().getApplication());
        telephone = am.getTelephone();
        if (am.isOnline()){
            tv_telephone.setText(telephone);
            tv_telephone.setTextSize(30);
            tv_login_tips.setVisibility(View.GONE);
            ll_login.setClickable(false);
        }

        //收藏
        Button btnCollection = view.findViewById(R.id.btn_collection);
        btnCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AccountManager am = AccountManager.getInstance(getActivity().getApplication());
                if (am.isOnline()){
                    Intent intent = new Intent(getActivity(), CollectionActivity.class);
                    startActivity(intent);
                }else {
                    ToastUtils.show(getActivity(),"登录后可使用收藏功能");
                }

            }
        });

        //关于
        Button brn_about_me = view.findViewById(R.id.btn_about_me);
        brn_about_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutMeActivity.class);
                startActivity(intent);
            }
        });

        //设置
        Button btn_setting = view.findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivityForResult(intent,REQUEST_LOGOUT_RESULT);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case REQUEST_PHONE:
                    tv_telephone.setText(data.getExtras().getString(LoginActivity.PARAM_PHONE));
                    tv_telephone.setTextSize(30);
                    tv_login_tips.setVisibility(View.GONE);
                    ll_login.setClickable(false);
                    break;
                case REQUEST_LOGOUT_RESULT:
                    tv_telephone.setText("登录");
                    tv_telephone.setTextSize(18);
                    tv_login_tips.setVisibility(View.VISIBLE);
                    tv_login_tips.setText("登录解锁更多功能");
                    ll_login.setClickable(true);
                    break;
                default:
                    LogUtils.e("请求码","请求码有误");
            }
        }
    }

}