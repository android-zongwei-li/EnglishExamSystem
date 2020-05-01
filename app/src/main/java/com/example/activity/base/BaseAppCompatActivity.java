package com.example.activity.base;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 抽出这个类的目的：
 * 1.为了更方便的分析activity的源码和启动流程（2020.2.24）
 */
public class BaseAppCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("Activity LifeRecycle","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Activity LifeRecycle","onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("Activity LifeRecycle","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("Activity LifeRecycle","onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v("Activity LifeRecycle","onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("Activity LifeRecycle","onDestroy");
    }

}
