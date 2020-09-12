package com.lzw.appupdater.net;

/**
 *
 * @author Li Zongwei
 * @date 2020/9/9
 **/
public interface INetCallback {
    /**
     * 请求成功，再此进行处理
     * @param response
     */
    void onSuccess(String response);

    /**
     * 请求失败，在此进行处理
     * @param throwable
     */
    void onFailed(Throwable throwable);
}
