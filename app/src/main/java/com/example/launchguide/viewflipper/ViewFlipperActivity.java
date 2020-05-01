package com.example.launchguide.viewflipper;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class ViewFlipperActivity extends Activity implements GestureDetector.OnGestureListener {

    private ViewFlipper viewFlipper;
    private Button btn;
    private LinearLayout llIndicator;

    private GestureDetector gestureDetector;
    private int index = 0;  //当前是第几屏

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewflipper);

        viewFlipper = findViewById(R.id.viewFlipper);

        btn = findViewById(R.id.btn);
        View view = View.inflate(this, R.layout.activity_viewpager,null);
        llIndicator = view.findViewById(R.id.ll_indicator);
        ininIndicator();

    }

    /**
     * 初始化指示器
     */
    private void ininIndicator(){
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                10f,getResources().getDisplayMetrics());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,width);
        layoutParams.rightMargin = 2*width;

        for(int i = 0; i < viewFlipper.getChildCount(); i++){
            View view = new View(this);
            view.setId(i);
            view.setBackgroundResource(i == 0 ? R.drawable.dot_focus: R.drawable.dot_normal);
            view.setLayoutParams(layoutParams);
            llIndicator.addView(view,i);
        }

        gestureDetector = new GestureDetector(this);

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1.getX() > e2.getX()){
            viewFlipper.showNext();
            index = index < 2 ? index + 1 : 0;
        }else if(e1.getX() < e2.getX()){
            viewFlipper.showPrevious();
            index = index < 0 ? index - 1 : 2;
        }else {
            return false;
        }
        changeIndicator();
        return true;
    }

    private void changeIndicator(){
        for(int i = 0; i < viewFlipper.getChildCount(); i++){
            llIndicator.getChildAt(i).setBackgroundResource(index == i ?
                    R.drawable.dot_focus: R.drawable.dot_normal);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}
