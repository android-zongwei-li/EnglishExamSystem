package com.example.view;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

public class CommonControlBar extends RelativeLayout {

    private Chronometer chronometer;
    private Button btn_chronometer;
    private Button btn_reference;
    private ImageView iv_icon_collection;
    private TextView tv_choose_word_num;

    // 记录是否在计时
    private boolean isStarting = false;
    // 记录下来的总时间
    private long recordingTime = 0;

    // 记录是否已经收藏
    private boolean isCollected = false;

    public CommonControlBar(Context context) {
        super(context);
    }

    public CommonControlBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonControlBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CommonControlBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        chronometer = (Chronometer) getChildAt(0);
        btn_chronometer = (Button) getChildAt(1);
        initChronometer();

        btn_reference = (Button) getChildAt(2);
        initBtnReference();

        iv_icon_collection = (ImageView) getChildAt(3);
        initIvCollection();

        tv_choose_word_num = (TextView) getChildAt(4);
        initTvChooseWordNum();

    }

    /**
     * 设置计时器和控制按钮
     */
    private void initChronometer(){

        btn_chronometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStarting){
                    isStarting = true;
                    // 跳过已经记录了的时间，起到继续计时的作用
                    chronometer.setBase(SystemClock.elapsedRealtime() - recordingTime);
                    Log.v(" recordingTime",recordingTime+"");
                    chronometer.start();
                    btn_chronometer.setText("暂停");
                }else {
                    isStarting = false;
                    chronometer.stop();
                    // 保存这次记录了的时间
                    recordingTime = SystemClock.elapsedRealtime()- chronometer.getBase();
                    Log.v(" recordingTime",recordingTime+"");
                    btn_chronometer.setText("开始");
                }
            }
        });
    }

    /**
     * 点击参考按钮后，弹出参考模块
     */
    private void initBtnReference(){
        btn_reference.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"参考",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 点击收藏控件后，换图、执行收藏任务
     */
    private void initIvCollection(){
        iv_icon_collection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isCollected){
                    isCollected = true;
                    iv_icon_collection.setImageResource(R.drawable.icon_collection_after);
                    Toast.makeText(getContext(),"已收藏",Toast.LENGTH_SHORT).show();
                }else {
                    isCollected = false;
                    iv_icon_collection.setImageResource(R.drawable.icon_collection_befor);
                    Toast.makeText(getContext(),"取消收藏",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     *  题目序号和总数显示控制
     */
    private void initTvChooseWordNum(){

    }

    public boolean isCollected() {
        return isCollected;
    }
}
