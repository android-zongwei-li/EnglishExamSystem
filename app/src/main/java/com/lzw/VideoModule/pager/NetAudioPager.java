package com.lzw.VideoModule.pager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lzw.VideoModule.base.BasePager;

/**
 * 网络音频页面
 */
public class NetAudioPager extends BasePager {

    private static final String TAG = NetAudioPager.class.getSimpleName();

    private TextView textView;

    public NetAudioPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        Log.i(TAG,"网络音频页面初始化了");
        textView = new TextView(context);
        textView.setTextSize(25);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        Log.i(TAG,"网络音频页面data初始化了");
        textView.setText("NetAudioPager");
    }

}
