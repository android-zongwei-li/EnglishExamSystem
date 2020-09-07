package com.lzw.utils;

import android.os.Environment;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * 文件工具类，提供一些用于操作文件的方法
 */
public class FileUtils {
    private static final String TAG = "FileUtils";

    private static File file;

    /**
     * （在外部存储器中）创建txt文件
     *
     * @param fileName 文件名，不含后缀
     */
    public static void createTxtFile(String fileName) {
        try {
            file = new File(Environment.getExternalStorageDirectory(), fileName + ".txt");
            if (!file.exists()) {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "Error on write File:" + fileName + e);
        }
    }

    private static RandomAccessFile raf;

    /**
     * 向txt文件中写入数据（string）
     *
     * @param strContent 要写入文件的数据（string字符串）
     */
    public static void writeTxtToFile(String strContent) {
        // 每次写入时，都换行写
        String content = strContent + "\r\n";
        try {
            if (raf == null) {
                raf = new RandomAccessFile(file, "rwd");
            }
            raf.seek(file.length());
            raf.write(content.getBytes());
            raf.close();
        } catch (Exception e) {
            LogUtils.e(TAG, "Error on write File:" + e);
        }
    }

}
