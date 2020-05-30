package com.example.activity.activity_in_fragment3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.utils.AccountManager;
import com.example.utils.ToastUtils;
import com.example.view.topbar.TopBar;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        TopBar topBar = findViewById(R.id.topBar);
        topBar.setTitle(R.string.title_setting);
        topBar.setRighttIsVisable(false);
        topBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });

        //设置按钮
        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountManager am = AccountManager.getInstance(getApplication());
                if (am.isOnline()){
                    am.logout();
                    setResult(RESULT_OK);
                    finish();
                }else {
                    ToastUtils.show(SettingActivity.this,"当前未登录");
                }
            }
        });

    }
}
