package com.example.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beans.Word;
import com.example.myapplication.R;
import com.example.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by: lzw.
 * Date: 2020/5/24
 * Time: 10:32
 * Description：
 * 1、
 *
 * 单词列表适配器
 */
public class WordsListRVAdapter extends RecyclerView.Adapter {
    private static final String TAG = "UnfamiliarWordsRVAdapter";
    private ArrayList<Word> words;

    public WordsListRVAdapter(ArrayList<Word> words){
        this.words = words;

        LogUtils.e("words列表",words.toString());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (words.size() == 0){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_null,parent,false);
            WordsListRVAdapter.MyViewHolder myViewHolder = new WordsListRVAdapter.MyViewHolder(view);
            return myViewHolder;
        }else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.word_list_item,parent,false);
            WordsListRVAdapter.MyViewHolder myViewHolder = new WordsListRVAdapter.MyViewHolder(view);
            return myViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (words.size() == 0){
            LogUtils.d(TAG,"无生词");
            TextView tvCollectionNull = holder.itemView.findViewById(R.id.tv_collection_null);
            tvCollectionNull.setText("无学习记录");
        }else {
            TextView tv_word = holder.itemView.findViewById(R.id.tv_word);
            TextView tv_chinese = holder.itemView.findViewById(R.id.tv_chinese);

            tv_word.setText(words.get(position).getWord());
            tv_chinese.setText(words.get(position).getChinese());
        }
    }

    @Override
    public int getItemCount() {
        return words.size()>0?words.size():1;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
