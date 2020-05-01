package com.example.utils;

import android.util.Log;

import com.example.beans.Word;

/**
 * 对log进行封装
 * 1、控制log的输出
 * 2、输出较长的日志：
 * 在获取word数据时，Log输出看结果，不能看到所有的，所以通过这个类处理一下
 * 3、
 */
public class LogUtils {

    private static final int VERBOSE = 0;
    private static final int DEBUG = 1;
    private static final int INFO = 2;
    private static final int WARN = 3;
    private static final int ERROR = 4;
    private static final int NOTHING = 5;

    private static int level = VERBOSE;//发布前改为 NOTHING

    public static void v(String tag, String msg){
        if (level <= VERBOSE){
            Log.v(tag,msg);
        }
    }

    public static void d(String tag, String msg){
        if (level <= DEBUG){
            Log.d(tag,msg);
        }
    }

    public static void i(String tag, String msg){
        if (level <= INFO){
            Log.i(tag,msg);
        }
    }

    public static void w(String tag, String msg){
        if (level <= WARN){
            Log.w(tag,msg);
        }
    }

    public static void e(String tag, String msg){
        if (level <= ERROR){
            Log.e(tag,msg);
        }
    }

    /**
     * 截断输出日志
     * @param msg
     */
    public static void longlog(String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;

        int segmentSize = 3 * 1024;
        long length = msg.length();
        // 长度小于等于限制直接打印
        if (length <= segmentSize ) {
            Log.e(tag, msg);
        }else {
            // 循环分段打印日志
            while (msg.length() > segmentSize ) {
                String logContent = msg.substring(0, segmentSize );
                msg = msg.replace(logContent, "");
                Log.e(tag, logContent);
            }
            // 打印剩余日志
            Log.e(tag, msg);
        }
    }

}
