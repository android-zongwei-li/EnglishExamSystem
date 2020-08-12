package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;

import com.example.utils.LogUtils;

import java.io.FileDescriptor;
import java.io.PrintWriter;

public class BaseService extends Service {
    private final String TAG = getClass().getSimpleName();
    private final String SERVICE_LIEF_RECYCLE_TAG = "service:life recycle:"+TAG;

    public BaseService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.v(SERVICE_LIEF_RECYCLE_TAG,"onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.v(SERVICE_LIEF_RECYCLE_TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.v(SERVICE_LIEF_RECYCLE_TAG,"onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.v(SERVICE_LIEF_RECYCLE_TAG,"onBind");

        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.v(SERVICE_LIEF_RECYCLE_TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.v(SERVICE_LIEF_RECYCLE_TAG,"onConfigurationChanged");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtils.v(SERVICE_LIEF_RECYCLE_TAG,"onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtils.v(SERVICE_LIEF_RECYCLE_TAG,"onTrimMemory");
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        LogUtils.v(SERVICE_LIEF_RECYCLE_TAG,"onRebind");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        LogUtils.v(SERVICE_LIEF_RECYCLE_TAG,"onTaskRemoved");
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(fd, writer, args);
        LogUtils.v(SERVICE_LIEF_RECYCLE_TAG,"dump");
    }
}
