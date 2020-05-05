/**
 * @(#)ToastUtils.java, 2014-08-01
 *
 * Copyright 2014 Youdao, Inc. All rights reserved.
 * YOUDAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.example.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.MyApplication;

public class ToastUtils {

	private static Toast toast = null;

	public static void show(String msg) {
		showText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT);
	}

	public static void show(Integer stringId) {
		showText(MyApplication.getInstance(), stringId, Toast.LENGTH_SHORT);
	}

	public static void show(Context context, String msg) {
		showText(context, msg, Toast.LENGTH_SHORT);
	}

	public static void show(Context context, Integer stringId) {
		showText(context, stringId, Toast.LENGTH_SHORT);
	}

	private static void showText(Context context, String msg, int length) {
		if (toast == null) {
			toast = Toast.makeText(context, msg, length);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}

	private static void showText(Context context, Integer stringId, int length) {
		if (toast == null) {
			toast = Toast.makeText(context, stringId, length);
		} else {
			toast.setText(stringId);
		}
		toast.show();
	}
}