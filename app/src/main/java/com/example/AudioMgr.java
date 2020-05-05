/**
 * @(#)AudioMgr.java, 2015年12月14日.
 * <p>
 * Copyright 2015 Yodao, Inc. All rights reserved.
 * YODAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.example;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.util.Log;

import com.youdao.sdk.common.YouDaoLog;
import com.youdao.sdk.common.util.AsyncTasks;

/**
 * @author lukun
 */
public class AudioMgr {
    public static final String PLAY_LOG = "TranslatePlay ：";

    public interface SuccessListener {
        public void success();

        public void playover();
    }

    public static void startPlayVoice(String url, SuccessListener listener) {
        try {
            AsyncTasks.safeExecuteOnExecutor(new PlayTask(url, listener));
        } catch (Exception e) {
            YouDaoLog.e(AudioMgr.PLAY_LOG + "AudioMgr startPlayVoice", e);
            Log.d("AudioMgr", "fail to fetch data: ", e);
        }
    }

    static class PlayTask extends AsyncTask<Void, Void, Void> {
        private String mUrl;

        private SuccessListener listener;

        public PlayTask(String url, SuccessListener listener) {
            mUrl = url;
            this.listener = listener;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (isCancelled()) {
                PlayMgr.getInstance().getMediaPlayer().stop();
                onCancelled();
                return;
            }
        }

        @Override
        protected void onCancelled() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            MediaPlayer mediaPlayer = PlayMgr.getInstance()
                    .getMediaPlayer();
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    YouDaoLog.e(AudioMgr.PLAY_LOG + "AudioMgr playTask play onCompletion");
                    if (listener != null) {
                        listener.playover();
                    }
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    YouDaoLog.e(AudioMgr.PLAY_LOG + "AudioMgr playTask on prepareOk");
                    mp.start();
                    YouDaoLog.e(AudioMgr.PLAY_LOG + "AudioMgr playTask play onStart");
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    YouDaoLog.e(AudioMgr.PLAY_LOG + "AudioMgr playTask onError");
                    return false;
                }
            });
            try {
                mediaPlayer.reset();

//                Uri uri;
//
//                uri = Uri.parse(mUrl);
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Accept-Encoding","gzip, deflate");
//                headers.put("Accept","*/*");
                mediaPlayer.setDataSource(mUrl);
                mediaPlayer.prepareAsync();// 进行缓冲\
            } catch (Exception e) {
                e.printStackTrace();
                YouDaoLog.e(AudioMgr.PLAY_LOG + "AudioMgr playTask prepare error = " + e.toString());
            }
            return null;
        }
    }

}
