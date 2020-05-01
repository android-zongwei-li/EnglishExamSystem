package com.example.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.example.myapplication.R;
import com.example.utils.Utils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 一个简单的音频播放控件，此APP主要用于播放听力材料
 * 主要包括三个部分：
 * 1.顶部进度条
 * 2.播放时长记录、总时长显示
 * 3.开始/暂停按钮
 *
 * 问题记录：
 * 1.android.content.res.Resources$NotFoundException: String resource ID
 * https://blog.csdn.net/qq_36478274/article/details/104879342
 * 2.Only the original thread that created a view hierarchy can touch its views
 * https://blog.csdn.net/qq_36478274/article/details/104879499
 * 3.Android Studio在写XML布局的时候控件属性不自动提示
 * https://blog.csdn.net/qq_36478274/article/details/104861862
 */
public class SimpleAudioPlayer extends RelativeLayout implements View.OnClickListener {

    private Context context;

    private SeekBar seekBar;
    private RelativeLayout rl;
    private TextView tv_current_position;
    private ImageView iv_pause;
    private TextView tv_duration;

    private static int resourceId; //资源id

    // 播放听力
    private MediaPlayer mediaPlayer = new MediaPlayer();
    //
    // 当前音频播放进度
    private int currentPosition;
    //互斥变量，防止进度条与定时器冲突
    private boolean isSeekBarChanging;

    private Timer timer;

    // 音频时长
    int duration;

    private Utils utils = new Utils();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            tv_current_position.setText(utils.stringForTime(currentPosition));
        }
    };


    public SimpleAudioPlayer(Context context) {
        this(context,null);
    }

    public SimpleAudioPlayer(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);

    }

    public SimpleAudioPlayer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public SimpleAudioPlayer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        seekBar = (SeekBar) getChildAt(0);

        rl = (RelativeLayout) getChildAt(1);

        tv_current_position = (TextView) rl.getChildAt(0);
        iv_pause = (ImageView) rl.getChildAt(1);
        tv_duration = (TextView) rl.getChildAt(2);

        iv_pause.setOnClickListener(this);

        initMediaPlayer();
    }

    /**
     * 初始化播放器
     */
    private void initMediaPlayer() {

        Uri uri = Uri.parse("android.resource://com.example.myapplication/"+resourceId);
            //       File file = new File(Environment.getExternalStorageDirectory(), "music.mp3");
        try {
            mediaPlayer.setDataSource(context,uri);//指定音频文件路径
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setLooping(true);//设置为循环播放
        mediaPlayer.prepareAsync();//数据缓冲
        /*
        监听缓存事件，初始化seekbar
         */
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //
                mp.seekTo(currentPosition);
                //
                duration = mediaPlayer.getDuration();
                tv_duration.setText(utils.stringForTime(duration));
                //
                setSeekBar();

            }

        });

    }

    /**
     * 设置seekbar的相关属性，和mediaplayer绑定
     */
    private void setSeekBar(){
        seekBar.setMax(duration);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            /*
            滚动时，暂停定时器
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekBarChanging = true;
            }

            /*
            滑动结束，重新设置播放位置
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekBarChanging = false;
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        // 使seekbar的进度和当前播放进度同步
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isSeekBarChanging){
                    currentPosition = mediaPlayer.getCurrentPosition();// 记录当前播放位置
                    seekBar.setProgress(currentPosition);
                    // 发送消息，通知UI线程刷新视图
                    handler.sendEmptyMessage(0);
                }
            }
        },0,10);
    }

    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        //开始(暂停)按钮的监听
        if (v.getId() == iv_pause.getId()){
            tv_current_position.setVisibility(View.VISIBLE);
            tv_duration.setVisibility(View.VISIBLE);

            //如果没在播放中，立刻开始播放。
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
                iv_pause.setImageDrawable(getResources().getDrawable(R.drawable.ic_playing));

            } else{ //如果在播放中，立刻暂停。
                mediaPlayer.pause();
                iv_pause.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
            }
               /* //如果在播放中，立刻停止。
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.reset();
                    initMediaPlayer();//初始化播放器 MediaPlayer
                }*/
        }

    }

    /**
     * 这个方法用于释放资源
     * 应该是必须要调用的，有没有什么方法可以让外部知道并一定调用这个方法呢
     */
    public void finishPlay(){
        timer.purge();
        timer.cancel();
        timer = null;

        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;

        handler = null;
    }

    public static void setResourceId(@RawRes int resourceId) {
        SimpleAudioPlayer.resourceId = resourceId;
    }
}
