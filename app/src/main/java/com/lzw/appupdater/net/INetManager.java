package com.lzw.appupdater.net;

import java.io.File;

/**
 * @author Li Zongwei
 * @date 2020/9/9
 **/
public interface INetManager {

    /**
     * 发起请求
     *
     * @param url         地址
     * @param netCallback 处理返回的结果
     * @param tag         标识当前的请求
     */
    void get(String url, INetCallback netCallback, Object tag);

    /**
     * 下载
     *
     * @param url              资源地址
     * @param targetFile       保存到：targetFile
     * @param downloadCallback 下载结果回调
     * @param tag              标识当前的下载请求
     */
    void download(String url, File targetFile, IDownloadCallback downloadCallback, Object tag);

    /**
     * 取消数据请求
     *
     * @param tag 标识要取消的请求
     */
    void cancel(Object tag);
}
