package com.example.activity.acticity_in_fragment1;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = "WebViewActivity";
    public static final String URL = "Url";//此页面所需url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String url = intent.getStringExtra(URL);
        setContentView(R.layout.activity_web_view);
        WebView webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);//webView支持JavaScript脚本
        webView.setWebViewClient(new WebViewClient());//当在网页中跳到其他网页时，
                                                    // 仍然在webView中显示，而非调用系统浏览器
        webView.loadUrl(url);
    }
}
