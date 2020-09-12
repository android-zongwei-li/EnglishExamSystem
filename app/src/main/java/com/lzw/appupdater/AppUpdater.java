package com.lzw.appupdater;

import com.lzw.appupdater.net.INetManager;
import com.lzw.appupdater.net.OkHttpNetManager;

/**
 * 负责App的更新
 *
 * @author Li Zongwei
 * @date 2020/9/9
 **/
public class AppUpdater {

    private static AppUpdater sInstance = new AppUpdater();

    public static AppUpdater getInstance() {
        return sInstance;
    }

    /**
     * 默认的网络访问方式：OkHttpNetManager
     */
    private static INetManager sINetManager = new OkHttpNetManager();

    public INetManager getINetManager() {
        return sINetManager;
    }

    /**
     * 指定网络访问方式
     *
     * @param netManager
     */
    public void setINetManager(INetManager netManager) {
        sINetManager = netManager;
    }
}
