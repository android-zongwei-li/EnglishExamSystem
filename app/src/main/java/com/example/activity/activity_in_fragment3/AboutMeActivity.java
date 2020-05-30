package com.example.activity.activity_in_fragment3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.tv.TvContentRating;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.utils.ToastUtils;
import com.example.view.topbar.TopBar;

public class AboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        TopBar topBar = findViewById(R.id.topBar);
        topBar.setTitle(R.string.title_about_me);
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

        TextView tv_add_qq = findViewById(R.id.tv_add_qq);
        tv_add_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=893846649"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    ToastUtils.show(AboutMeActivity.this,"请检查是否安装QQ");
                }
            }
        });

        TextView tv_project_address = findViewById(R.id.tv_project_address);
        tv_project_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://github.com/yaotiaoshunv/EnglishExamSystem"));
                    startActivity(intent);
            }
        });
        tv_project_address.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager) AboutMeActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText("https://github.com/yaotiaoshunv/EnglishExamSystem");
                cm.getText();

                ToastUtils.show(AboutMeActivity.this,"已复制");
                return true;
            }
        });

    }

}
