package com.lzw.utils.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Li Zongwei
 * @date 2020/9/9
 **/
public class AppUtils {

    /**
     * 获取当前App的版本号
     *
     * @return  版本号
     */
    public static long getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                long longVersionCode = packageInfo.getLongVersionCode();
                return longVersionCode;
            }else {
                return packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * MD5校验
     *
     * @param targetFile    要校验md5的文件
     * @return              文件的md5
     */
    public static String getFileMd5(File targetFile) {
        if (targetFile == null || !targetFile.isFile()){
            return null;
        }

        MessageDigest digest;
        FileInputStream fis = null;
        byte[] buffer = new byte[1024];
        try {
            digest = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(targetFile);
            int bufferLen;
            while ((bufferLen = fis.read(buffer)) != -1){
                digest.update(buffer,0,bufferLen);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        byte[] result = digest.digest();
        BigInteger bigInteger = new BigInteger(1,result);

        return bigInteger.toString(16);
    }

    /**
     * 安装apk
     *
     * @param activity
     * @param apkFile
     */
    public static void installApk(FragmentActivity activity, File apkFile) {
        //文件有所有者概念，现在是属于当前进程的，需要把这个文件暴露给系统安装程序（其他进程）去安装
        //因此，可能会存在权限问题，需要做下面的设置
        //如果文件是sdcard上的，就不需要这个操作了
        try {
            apkFile.setExecutable(true, false);
            apkFile.setReadable(true, false);
            apkFile.setWritable(true, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri;

        //TODO N FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }else {
            uri = Uri.fromFile(apkFile);
        }

        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        activity.startActivity(intent);

        //TODO O INSTALL PERMISSION
        //在AndroidManifest中加入权限:    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>
    }
}
