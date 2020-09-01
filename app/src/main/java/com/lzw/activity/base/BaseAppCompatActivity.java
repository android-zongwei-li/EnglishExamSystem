package com.lzw.activity.base;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.lzw.utils.LogUtils;

/**
 * 抽出这个类的目的：
 * 1、为了更方便的分析activity的源码和启动流程（2020.2.24）
 * 2、用于观察activity的生命周期变化
 */
public class BaseAppCompatActivity extends AppCompatActivity {
    public final String TAG = getClass().getSimpleName();
    //这个变量用于观察activity的生命周期变化情况
    private final String LIEF_RECYCLE_TAG = "life recycle:"+TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.v(LIEF_RECYCLE_TAG,"onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.v(LIEF_RECYCLE_TAG,"onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.v(LIEF_RECYCLE_TAG,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.v(LIEF_RECYCLE_TAG,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.v(LIEF_RECYCLE_TAG,"onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.v(LIEF_RECYCLE_TAG,"onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.v(LIEF_RECYCLE_TAG,"onDestroy");
    }

    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
            }
        }
        return super.dispatchTouchEvent(me);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
