package com.lzw.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzw.beans.MediaItem;
import com.lzw.englishExamSystem.R;
import com.lzw.utils.Utils;

import java.util.ArrayList;

/**
 * VideoPager的适配器
 */
public class VideoPagerAdapter extends BaseAdapter {

    private Context context;
    private final ArrayList<MediaItem> mediaItems;
    private Utils utils;

    public VideoPagerAdapter(Context context, ArrayList<MediaItem> mediaItems){
        this.context = context;
        this.mediaItems = mediaItems;
        utils = new Utils();
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = View.inflate(context, R.layout.item_video_pager,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_video_icon = convertView.findViewById(R.id.iv_video_icon);
            viewHolder.tv_video_name = convertView.findViewById(R.id.tv_video_name);
            viewHolder.tv_video_duration = convertView.findViewById(R.id.tv_video_duration);
            viewHolder.tv_video_size = convertView.findViewById(R.id.tv_video_size);

            convertView.setTag(viewHolder);
        }else {
            convertView.getTag();
        }

        //根据position得到列表中对应位置的数据
        MediaItem mediaItem = mediaItems.get(position);
        if(viewHolder != null){
            viewHolder.tv_video_name.setText(mediaItem.getName());
            viewHolder.tv_video_size.setText(Formatter.formatFileSize(context,mediaItem.getSize()));
            viewHolder.tv_video_duration.setText(utils.stringForTime(mediaItem.getDuration().intValue()));
        }


        return convertView;
    }

    static class ViewHolder{
        ImageView iv_video_icon;
        TextView tv_video_name;
        TextView tv_video_duration;
        TextView tv_video_size;
    }

}