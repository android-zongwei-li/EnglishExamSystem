package com.lzw.view.topbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.lzw.englishExamSystem.R;

/**
 * 顶部导航栏控件
 * 这是一个组合控件，由左右两个Button和一个TextView组成
 */
public class TopBar extends RelativeLayout {

    // 步骤2：定义需要使用的控件
    private Button leftButton , rightButton;
    private TextView tvTitle;

    // 步骤3：声明这些控件所需要使用的属性，即之前在 atts.xml 中定义的属性

    // 左 Button 属性
    private String leftText;
    private int leftTextColor;
    private Drawable leftBackground;

    // 右 Button 属性
    private String rightText;
    private int rightTextColor;
    private Drawable rightBackground;

    // 中间 TextView 属性
    private String title;
    private int titleTextColor;
    private float titleTextSize;

    private LayoutParams leftParams,rightParams,titleParams;

    private topbarClickListener listener;

    public interface topbarClickListener{
        void leftClick();
        void rightClick();
    }

    public void setOnTopBarClickListener(topbarClickListener listener){
        this.listener = listener;
    }


    /**
     * 步骤1：添加构造方法
     *        需要自定义属性就使用这一个构造方法，使用atts参数
     * @param context
     * @param attrs
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public TopBar(final Context context, AttributeSet attrs) {
        super(context, attrs);

        // 步骤4：给声明好的属性赋值，以将属性和控件关联
        // 4.1  通过TypedArray存储从xml文件中获取到的自定义属性的值，并赋给相应的变量
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);

        leftText = ta.getString(R.styleable.TopBar_leftText);
        leftTextColor = ta.getColor(R.styleable.TopBar_leftTextColor,0);
        leftBackground = ta.getDrawable(R.styleable.TopBar_leftBackground);

        rightText = ta.getString(R.styleable.TopBar_rightText);
        rightTextColor = ta.getColor(R.styleable.TopBar_rightTextColor,0);
        rightBackground = ta.getDrawable(R.styleable.TopBar_rightBackground);

        title = ta.getString(R.styleable.TopBar_title);
        titleTextSize = ta.getDimension(R.styleable.TopBar_titleTextSize,0);
        titleTextColor = ta.getColor(R.styleable.TopBar_titleTextColor,0);

        // 回收。1.避免浪费资源 2.避免由于缓存而引起的错误
        ta.recycle();

        // 4.2  实例化控件
        leftButton = new Button(context);
        rightButton = new Button(context);
        tvTitle = new Button(context);

        // 4.3  将属性值和控件关联
        leftButton.setText(leftText);
        leftButton.setTextColor(leftTextColor);
        leftButton.setBackground(leftBackground);

        rightButton.setText(rightText);
        rightButton.setTextColor(rightTextColor);
        rightButton.setBackground(rightBackground);

        tvTitle.setText(title);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setTextColor(titleTextColor);
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setBackgroundColor(Color.argb(0,0,0,0));

        setBackgroundColor(0xFFFFEB3B);

        //  步骤5：将控件放到layout中，通过LayoutParams控制摆放方式
        leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);

        addView(leftButton, leftParams);

        rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);

        addView(rightButton, rightParams);

        titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);

        addView(tvTitle, titleParams);

        //  步骤6：动态控制 TopBar
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftClick();
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rightClick();
            }
        });

    }

    /**
     * 是否显示左Button
     * @param flag
     */
    public void setLeftIsVisable(boolean flag){
        if(flag){
            leftButton.setVisibility(View.VISIBLE);
        }else {
            leftButton.setVisibility(View.GONE);
        }
    }

    /**
     * 是否显示右Button
     * @param flag
     */
    public void setRighttIsVisable(boolean flag){
        if(flag){
            rightButton.setVisibility(View.VISIBLE);
        }else {
            rightButton.setVisibility(View.GONE);
        }
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitle(int title) {
        tvTitle.setText(title);
    }

    public void setRightButtonText(String title) {
        rightButton.setText(title);
    }

    public void setRightButtonText(int title) {
        rightButton.setText(title);
    }
}
