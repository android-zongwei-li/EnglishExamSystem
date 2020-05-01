package com.example.VideoModule.pager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.VideoModule.base.BasePager;

/**
 * 本地音频页面
 */
public class AudioPager extends BasePager {

    private static final String TAG = AudioPager.class.getSimpleName();

    private TextView textView;

    public AudioPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        Log.i(TAG,"本地音频页面初始化了");
        textView = new TextView(context);
        textView.setTextSize(25);
        return textView;
    }

    @Override
    public void initData() {
        Log.i(TAG,"本地音频页面data初始化了");
        super.initData();
        textView.setText("AudioPager");
    }

}
