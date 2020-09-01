package com.lzw.activity.activity_in_fragment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lzw.utils.GlideImageLoader;

public class ShowImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String imgUrl = intent.getStringExtra("url");

        ImageView img = new ImageView(this);
        GlideImageLoader imageLoader = new GlideImageLoader();
        imageLoader.displayImage(this,imgUrl,img);
        setContentView(img);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
