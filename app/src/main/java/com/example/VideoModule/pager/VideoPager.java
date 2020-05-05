package com.example.VideoModule.pager;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.adapter.VideoPagerAdapter;
import com.example.VideoModule.base.BasePager;
import com.example.VideoModule.videoactivity.SystemVideoPlayer;
import com.example.beans.MediaItem;

import com.example.myapplication.R;

import java.util.ArrayList;

/**
 * 本地视频页面
 */
public class VideoPager extends BasePager {

    private static final String TAG = VideoPager.class.getSimpleName();

    private ListView listView;

    private TextView tv_nomedia;

    private ProgressBar progressBar;

    private VideoPagerAdapter videoPagerAdapter;

    // 装数据集合
    private ArrayList<MediaItem> mediaItmes;

    public VideoPager(Context context) {
        super(context);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (mediaItmes != null && mediaItmes.size() > 0){
                //有数据
                //设置适配器
                videoPagerAdapter = new VideoPagerAdapter(context,mediaItmes);
                listView.setAdapter(videoPagerAdapter);
                //文本隐藏
                tv_nomedia.setVisibility(View.GONE);
            }else{
                //没有数据
                //文本显示
                tv_nomedia.setVisibility(View.VISIBLE);
            }
            //ProgressBar隐藏
            progressBar.setVisibility(View.GONE);
        }
    };

    @Override
    public View initView() {
        Log.i(TAG,"本地视频页面初始化了");
        View view = View.inflate(context, R.layout.video_pager,null);
        listView = view.findViewById(R.id.listview);
        tv_nomedia = view.findViewById(R.id.tv_nomedia);
        progressBar = view.findViewById(R.id.pb_loading);
        // 设置ListView的Item的点击时间
        listView.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.i(TAG,"本地视频页面data初始化了");
        //加载本地视频数据
        getDataFromLocal();
    }

    /**
     * 从本地的sdcard得到数据
     * //1.遍历sdcard，根据后缀名（一般不使用，当数据较多时，遍历速度较慢）
     * //2.从内容提供者里面获取视频
     * //3.如果是6.0以上，要动态获取读取sdcard的权限
     */
    private void getDataFromLocal() {
        mediaItmes = new ArrayList<>();

        new Thread(){
            @Override
            public void run() {
                super.run();

                ContentResolver resolver = context.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Video.Media.DISPLAY_NAME,    //视频文件在sdcard的名称
                        MediaStore.Video.Media.DURATION,        //视频总时长
                        MediaStore.Video.Media.SIZE,            //视频文件大小
                        MediaStore.Video.Media.DATA,            //视频的绝对地址
                        MediaStore.Video.Media.ARTIST,          //歌曲的演唱者
                };
                Cursor cursor = resolver.query(uri,objs,null,null,null);
                if(cursor != null){
                    while(cursor.moveToNext()){

                        MediaItem mediaItem = new MediaItem();

                        mediaItmes.add(mediaItem);

                        String name = cursor.getString(0);//视频名称
                        mediaItem.setName(name);

                        long duration = cursor.getLong(1);//视频时长
                        mediaItem.setDuration(duration);

                        long size = cursor.getLong(2);//视频文件大小
                        mediaItem.setSize(size);

                        String data = cursor.getString(3);//视频播放地址
                        mediaItem.setData(data);

                        String artist = cursor.getString(4);//歌曲演唱者
                        mediaItem.setArtist(artist);
                    }

                    cursor.close();
                }

                // Handle 发消息（不管cursor是否为null,所以不写在if里面）
                handler.sendEmptyMessage(0);

            }
        }.start();

    }

    /**
     * 解决Android6.0以上版本不能读取外部存储权限的问题
     * 哪里需要读写内存卡的权限，就调用这个方法，必须在一个Activity里，
     * 同时功能清单文件也要配置读取权限
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            },1);
            return false;
        }
        return true;
    }

    private class MyOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           MediaItem mediaItem =  mediaItmes.get(position);
            Toast.makeText(context,"mediaItems == "+mediaItem.toString(),Toast.LENGTH_SHORT).show();

            //1.调起系统所有的播放-隐式意图
/*            Intent intent = new Intent();
            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
            context.startActivity(intent);
          */
            //2.调用自己写的播放器-显示意图
            Intent intent = new Intent(context, SystemVideoPlayer.class);
            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
            context.startActivity(intent);
        }
    }
}
