package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beans.Writing;
import com.example.myapplication.R;
import com.example.utils.LogUtils;

import java.util.List;

/**
 * Created by: lzw.
 * Date: 2020/4/29
 * Time: 12:36
 * Description：
 * 1、
 */
public class CollectionRVAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Writing> mWritings;

    public CollectionRVAdapter(Context context,List writings){
        this.mContext = context;
        this.mWritings = writings;
        LogUtils.d("rv-null",mWritings.toString());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if (mWritings.size() == 0){
           View view = LayoutInflater.from(parent.getContext())
                   .inflate(R.layout.collection_item_null,parent,false);
           MyViewHolder myViewHolder = new MyViewHolder(view);
           LogUtils.d("rv-null","swwwww");
           return myViewHolder;
        }else {
           View view = LayoutInflater.from(parent.getContext())
                   .inflate(R.layout.collection_item,parent,false);
           MyViewHolder myViewHolder = new MyViewHolder(view);
           LogUtils.d("rv","swwwww");
           return myViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mWritings.size() == 0){
            // 没有收藏
            LogUtils.d("rv-null","wu ");
            TextView tvCollectionNull = holder.itemView.findViewById(R.id.tv_collection_null);
            tvCollectionNull.setText("无收藏");
        }else {
            LogUtils.d("rv-null","sssss");
            TextView tvYear = holder.itemView.findViewById(R.id.tv_collection_year);
            TextView tvMonth = holder.itemView.findViewById(R.id.tv_collection_month);
            TextView tvIndex = holder.itemView.findViewById(R.id.tv_collection_index);
            TextView tvContent = holder.itemView.findViewById(R.id.tv_collection_content);
            tvYear.setText(mWritings.get(position).getYear());
            tvMonth.setText(mWritings.get(position).getMonth());
            tvIndex.setText(mWritings.get(position).getIndex());
            tvContent.setText(mWritings.get(position).getQuestion());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"dian",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mWritings.size()>0?mWritings.size():1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
