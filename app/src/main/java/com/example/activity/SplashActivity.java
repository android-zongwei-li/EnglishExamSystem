package com.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.example.MainActivity;
import com.example.activity.base.BaseAppCompatActivity;
import com.example.myapplication.R;

/**
 *  引导页启动方式1 —— Splash
 *  在启动时，加载试卷资源
 *
 *  问题：
 *  App启动的时候，会白屏半秒或者几秒，这样的用户体验是不好的，
 *  修改启动页（Splash）的主题即可。
 *  参考：https://www.jianshu.com/p/68c715e02c82
 */
public class SplashActivity extends BaseAppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    // 延迟时间
    private static final long DELAY_TIME = 3000L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // DELAY_TIME 后执行到这里
                // 执行在主线程中。Handle()在哪个线程中new，Runnable在哪个线程中执行
                startMainActivity();
            }
        },DELAY_TIME);
    }

    private boolean isStartMainActivity = false;
    /**
     * 跳转到主页面，关闭当前页面
     */
    private void startMainActivity(){
        if(!isStartMainActivity){
            isStartMainActivity = true;
            Intent intent = new Intent(SplashActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG,"onTouchEvent == Action "+event.getAction());
        startMainActivity();
        return super.onTouchEvent(event);
    }

}
