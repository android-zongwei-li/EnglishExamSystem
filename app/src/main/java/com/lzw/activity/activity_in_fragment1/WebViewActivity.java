package com.lzw.activity.activity_in_fragment1;

import android.annotation.SuppressLint;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;


import androidx.appcompat.app.AppCompatActivity;

import com.lzw.englishExamSystem.R;
import com.lzw.view.topbar.TopBar;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = "WebViewActivity";
    protected static final String HYBRID = "HYBRID";
    public static final String URL = "Url";//此页面所需url
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        TopBar topBar = findViewById(R.id.topBar);
        topBar.setTitle(R.string.title_website);
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

//        Intent intent = getIntent();
//        String url = intent.getStringExtra(URL);
//
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
//
        initView();
   }

    private void initView() {
        webView = findViewById(R.id.web_view);
        webView.loadUrl(getIntent().getStringExtra(URL));

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBlockNetworkImage(false);
        webView.getSettings().setBlockNetworkLoads(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 5.1.1; vivo X6SPlus A Build/LMY47V; wv) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.84 Mobile Safari/537.36");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            injectJavaScript();
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {

                if (s.startsWith("https") || s.startsWith("http") ) {
                    webView.loadUrl(s);
                }

                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            private View myView;

            //自定义视频播放  如果需要启用这个，需要设置x5,自己实现全屏播放。目前的使用的x5的视频播放
            //如果是点击h5 vedio标签的播放，需要自己实现全屏播放
            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
                super.onShowCustomView(view, customViewCallback);
                ViewGroup parent = (ViewGroup) webView.getParent();
                parent.removeView(webView);

                // 设置背景色为黑色
                view.setBackgroundColor(WebViewActivity.this.getResources().getColor(R.color.colorBlack));
                parent.addView(view);
                myView = view;

            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                if (myView != null) {
                    ViewGroup parent = (ViewGroup) myView.getParent();
                    parent.removeView(myView);
                    parent.addView(webView);
                    myView = null;

                }
            }



        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
            }else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 注入javaScript对象
     */
    @SuppressLint("AddJavascriptInterface")
    private void injectJavaScript() {
        webView.addJavascriptInterface(getJavascriptInterface(), HYBRID);
    }

    /**
     * 返回 注入javaScript对象,必须继承 {@link BaseHYBRID}
     *
     * @return T extend BaseHYBRID
     */
    protected BaseHYBRID getJavascriptInterface() {
        return new BaseHYBRID();
    }
    /**
     * 公共的JS行为基类,封装通用的操作给第三方
     */
    public class BaseHYBRID {

    }

    @Override
    protected void onDestroy() {
        webView.stopLoading();
        webView.removeAllViews();
        webView.destroy();
        webView = null;
        super.onDestroy();
    }

}
