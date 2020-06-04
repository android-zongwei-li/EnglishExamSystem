/**
 * @(#)PlayMgr.java, 2014年10月13日. Copyright 2012 Yodao, Inc. All rights
 *                           reserved. YODAO PROPRIETARY/CONFIDENTIAL. Use is
 *                           subject to license terms.
 */
package com.example;

import android.media.MediaPlayer;

public class PlayMgr  {

	private static PlayMgr mgr;

	private PlayMgr() {
		super();
	}

	public synchronized static PlayMgr getInstance() {
		if (mgr == null) {
			mgr = new PlayMgr();
		}
		return mgr;
	}
 
	private static MediaPlayer mMediaPlayer;

	public synchronized MediaPlayer getMediaPlayer() {
		if (mMediaPlayer == null)
//			mMediaPlayer = MediaPlayer.create(DemoApplication.getInstance(), R.raw.office);
			mMediaPlayer = new MediaPlayer();
		return mMediaPlayer;
	}

	public synchronized void startMediaPlayer(String audioUrl) {
		MediaPlayer mediaPlayer = getMediaPlayer();
		mediaPlayer.reset();
		try {
			mediaPlayer.setDataSource(audioUrl);
			mediaPlayer.prepare();// 进行缓冲
			mediaPlayer.start();
		} catch (Exception e) {
		}
	} 
}
