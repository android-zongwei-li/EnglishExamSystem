package com.example.launchguide.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.example.MainActivity;
import com.example.beans.TestPaperManager;
import com.example.myapplication.R;
import com.example.utils.TxtUtils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 *  引导页启动方式1 —— Splash
 *  在启动时，加载试卷资源
 */
public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    // 延迟时间
    private static final long DELAY_TIME = 1000L;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // DELAY_TIME 后执行到这里
                // 执行在主线程中。Handle()在哪个线程中new，Runnable在哪个线程中执行
                startMainActivity();
                Log.i(TAG,"当前线程名称 == "+Thread.currentThread().getName());
            }
        },DELAY_TIME);

        initTestPaper();

        initWord();
    }

    private boolean isStartMainActivity = false;
    /**
     * 跳转到主页面，关闭当前页面
     */
    private void startMainActivity(){
        if(!isStartMainActivity){
            isStartMainActivity = true;
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
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

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    /**
     * 用于加载数据，所有试卷资源在启动app后就开始加载，加载后交给testPaperManager管理
     * 启用一个子线程加载，当数据从网络获取时，就可以避免阻塞主线程了
     */
    private void initTestPaper(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                TxtUtils txtUtils = new TxtUtils(getBaseContext());
                JSONObject jsonObject = txtUtils.rawTxtToJson(R.raw.cet4_201806_01_test_paper);

                TestPaperManager testPaperManager = TestPaperManager.getInstance();
                try {

                    testPaperManager.addTestPaper(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * 这个方法使用来读取word文件的，读取的目的是获取其中的指定string，
     * 然后使用这些string来初始化sqlite数据库
     */
    private void initWord(){
  //      WordUtils wordUtils = new WordUtils(getApplicationContext());   // 注：这里 传入  getBaseContext（）会出现错误 no suck file ，后面对着两个方法还要深入了解一下
        // 1、拿到text
        // 2、取出text中的指定内容
 //       wordUtils.getCet4Questions(R.raw.cet4_2016_06_01);
        // 3、把数据插入到数据库中

    }

}
