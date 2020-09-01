package com.lzw.VideoModule.base;

import android.content.Context;
import android.view.View;

public abstract class BasePager {

    public final Context context;

    public View rootView;

    // 标识 是否 执行 initData，避免多次初始化数据，尤其是当initData请求网络数据的时候，就尤为重要
    public boolean isInitData;

    public BasePager(Context context){
        this.context = context;
        rootView = initView();
    }

    /**
     * 强制子类实现特定效果
     * @return
     */
    public abstract View initView();

    /**
     * 当子页面需要初始化数据，联网请求数据，或者绑定数据的时候要重写该方法
     */
    public void initData(){

    }

}