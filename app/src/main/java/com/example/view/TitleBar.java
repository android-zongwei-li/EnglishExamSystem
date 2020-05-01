package com.example.view;

import android.content.ContentResolver;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

/**
 * 视频播放页面标题栏
 */
public class TitleBar extends LinearLayout implements View.OnClickListener {

    private Context context;

    // 这里使用View 是为了，即使以后布局中的控件改了类型(比如TextView改为ImageView)，这里还是兼容
    private View tv_search;
    private View rl_game;
    private View iv_history;

    /**
     * 在代码中实例化该类的时候使用这个方法
     * @param context
     */
    public TitleBar(Context context) {
        this(context,null);
    }

    /**
     * 当在布局文件使用该类的时候，Android系统通过这个构造方法实例化该类
     * @param context
     * @param attrs
     */
    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * 当需要设置样式的时候，可以使用该方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 当布局文件加载完成时，回调此方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 得到孩子的实例
        tv_search = getChildAt(1);
        rl_game = getChildAt(2);
        iv_history = getChildAt(3);

        //设置点击事件
        tv_search.setOnClickListener(this);
        rl_game.setOnClickListener(this);
        iv_history.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search://搜索
                Toast.makeText(context,"搜索",Toast.LENGTH_LONG).show();
                break;
            case R.id.rl_game://游戏
                Toast.makeText(context,"游戏",Toast.LENGTH_LONG).show();
                break;
            case R.id.iv_history://播放历史
                Toast.makeText(context,"播放历史",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
