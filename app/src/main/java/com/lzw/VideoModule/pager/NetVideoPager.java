package com.lzw.VideoModule.pager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lzw.VideoModule.base.BasePager;

/**
 * 网络视频页面
 */
public class NetVideoPager extends BasePager {

    private static final String TAG = NetVideoPager.class.getSimpleName();

    private TextView textView;

    public NetVideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        Log.i(TAG,"网络视频页面初始化了");
        textView = new TextView(context);
        textView.setTextSize(25);
        return textView;
    }

    @Override
    public void initData() {
        Log.i(TAG,"网络视频页面data初始化了");
        super.initData();
        textView.setText("NetVideoPager");
    }

}
