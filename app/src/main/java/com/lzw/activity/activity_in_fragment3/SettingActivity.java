package com.lzw.activity.activity_in_fragment3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lzw.appupdater.AppUpdater;
import com.lzw.appupdater.bean.AppVersionInfoBean;
import com.lzw.appupdater.net.INetCallback;
import com.lzw.appupdater.ui.UpdateVersionShowDialog;
import com.lzw.constans.Constants;
import com.lzw.englishExamSystem.R;
import com.lzw.utils.AccountManager;
import com.lzw.utils.ToastUtils;
import com.lzw.utils.app.AppUtils;
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

        //版本检测按钮
        Button btnCheckVersion = findViewById(R.id.btn_check_version);
        btnCheckVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUpdater.getInstance().getINetManager().get(Constants.Url.appUpdaterJsonUrl, new INetCallback() {
                    @Override
                    public void onSuccess(String response) {
                        //TODO 分析结果，看是否要更新

                        //1、解析json
                        //2、做版本适配
                        //如果需要更新
                        //3、弹窗
                        //4、点击下载

                        AppVersionInfoBean appVersionInfoBean = AppVersionInfoBean.parse(response);

                        if (appVersionInfoBean == null){
                            Toast.makeText(SettingActivity.this, "版本检测接口返回数据异常", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // TODO 检测是否需要更新
                        try {
                            long versionCode = Long.parseLong(appVersionInfoBean.getVersionCode());
                            if (versionCode <= AppUtils.getVersionCode(SettingActivity.this)){
                                Toast.makeText(SettingActivity.this, "已经是最新版本，无需更新", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Toast.makeText(SettingActivity.this, "版本检测接口返回版本号异常", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // TODO 弹出更新窗口
                        UpdateVersionShowDialog.show(SettingActivity.this,appVersionInfoBean);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        throwable.printStackTrace();
                        Toast.makeText(SettingActivity.this, "版本更新接口请求失败", Toast.LENGTH_SHORT).show();
                    }
                },SettingActivity.this);
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
