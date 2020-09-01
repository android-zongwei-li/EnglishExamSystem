package com.lzw.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * 这个类用于
 * 播放音频：从 有道单词发音API 请求到mp3并播放
 */
public class AudioService extends Service {
    private static final String TAG = "AudioService";

    public static final String QUERY = "query";

    private MediaPlayer mp;
    private String query; // 要查询的单词

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG,"初始化音频资源");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (query != null && !query.equals(intent.getStringExtra(AudioService.QUERY)) &&
                mp != null){
            mp.start();
        }else {
            String query = intent.getStringExtra(AudioService.QUERY);
            Uri uri = Uri.parse("http://dict.youdao.com/dictvoice?audio=" + query);

            mp = MediaPlayer.create(AudioService.this,uri);
            mp.start();

            // 音乐播放完毕的事件处理
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    // 无
                }
            });

            // 播放音频出错
            mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {

                    mp.release();

                    return false;
                }
            });

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.stop();
        mp.release();
    }

    // 继承Service必须实现的方法
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
