package com.example;

import android.app.Application;

import com.youdao.sdk.app.YouDaoApplication;

/**
 * 初始化有道翻译SDK
 */
public class MyApplication extends Application {

    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        if(YouDaoApplication.getApplicationContext() == null)
            YouDaoApplication.init(this, "337ba5320f895dd8");
        myApplication = this;
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

}