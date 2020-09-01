package com.lzw;

import android.app.Application;
import android.os.Process;

import com.lzw.utils.LogUtils;
import com.tencent.smtt.sdk.QbSdk;
import com.youdao.sdk.app.YouDaoApplication;

/**
 * 初始化
 */
public class MyApplication extends Application {

    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        //这段是为了尝试解决——启动后很久才显示界面，但问题并没有解决
        new Thread(new Runnable() {
            @Override
            public void run() {
                //设置线程的优先级，不与主线程抢资源
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                //子线程初始化第三方组件
                try {
                    //建议延迟初始化，可以发现是否影响其它功能，或者是崩溃！
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(YouDaoApplication.getApplicationContext() == null)
                    YouDaoApplication.init(MyApplication.this, "337ba5320f895dd8");

                QbSdk.initX5Environment(MyApplication.this, new QbSdk.PreInitCallback() {
                    @Override
                    public void onCoreInitFinished() {
                        //x5内核初始化完成回调接口此接口回调并表示已经加载起来了x5，
                        //有可能特殊情况下x5内核加载失败，切换到系统内核。
                    }

                    @Override
                    public void onViewInitFinished(boolean b) {
                        //x5內核初始化完成的回调，
                        //为true表示x5内核加载成功，
                        //否则表示x5内核加载失败，会自动切换到系统内核。
                        LogUtils.d("@@","加载内核是否成功: "+b);
                    }
                });

            }
        }).start();

        myApplication = this;
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

}