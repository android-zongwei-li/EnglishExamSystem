package com.lzw.activity.activity_in_fragment3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lzw.englishExamSystem.R;
import com.lzw.utils.AccountManager;
import com.lzw.utils.ToastUtils;
import com.lzw.view.topbar.TopBar;

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
