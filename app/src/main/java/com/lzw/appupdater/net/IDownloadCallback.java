package com.lzw.appupdater.net;

import java.io.File;

/**
 *
 * @author Li Zongwei
 * @date 2020/9/9
 **/
public interface IDownloadCallback {
    /**
     * 下载成功，在此处理
     * @param apkFile
     */
    void onSuccess(File apkFile);

    /**
     * 下载进度，在此处理
     * @param progress
     */
    void progress(int progress);

    /**
     * 下载失败，在此处理
     * @param throwable
     */
    void onFailure(Throwable throwable);
}
