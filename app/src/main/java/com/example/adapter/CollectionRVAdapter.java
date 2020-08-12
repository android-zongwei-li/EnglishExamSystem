package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beans.CollectedListening;
import com.example.beans.CollectedReading;
import com.example.beans.Translation;
import com.example.beans.Writing;
import com.example.myapplication.R;
import com.example.utils.LogUtils;
import com.example.utils.testPaperUtils.TestPaperFactory;
import com.example.utils.testPaperUtils.TestPaperFromWord;

import java.util.List;

/**
 * Created by: lzw.
 * Date: 2020/4/29
 * Time: 12:36
 * Description：
 * 1、
 */
public class CollectionRVAdapter extends RecyclerView.Adapter {
    private static final String TAG = "CollectionRVAdapter";
    private Context mContext;

    private int QuestionType;
    private static final int WRITING = 0;
    private static final int LISTENING = 1;
    private static final int READING = 2;
    private static final int TRANSLATION = 3;

    private List mList;
    private List<Writing> mWritings;
    private List<CollectedListening> mListenings;
    private List<CollectedReading> mReadings;
    private List<Translation> mTranslations;

    public CollectionRVAdapter(Context context,List list,int QuestionType){
        this.mContext = context;
        mList = list;
        this.QuestionType = QuestionType;
        switch (QuestionType){
            case WRITING:
                this.mWritings = list;
                LogUtils.d(TAG+"-mWriting",mWritings.toString());
                break;
            case LISTENING:
                this.mListenings = list;
                LogUtils.d(TAG+"-mListening",mListenings.toString());
                break;
            case READING:
                this.mReadings = list;
                LogUtils.d(TAG+"-mReading",mReadings.toString());
                break;
            case TRANSLATION:
                this.mTranslations = list;
                LogUtils.d(TAG+"-mTranslations",mTranslations.toString());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + QuestionType);
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mList.size() == 0){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_null,parent,false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.collection_item,parent,false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (mList.size() == 0){
            // 没有收藏
            LogUtils.d(TAG,"无收藏");
            TextView tvCollectionNull = holder.itemView.findViewById(R.id.tv_collection_null);
            tvCollectionNull.setText("无收藏");
        }else {
            TextView tvYear = holder.itemView.findViewById(R.id.tv_collection_year);
            TextView tvMonth = holder.itemView.findViewById(R.id.tv_collection_month);
            TextView tvIndex = holder.itemView.findViewById(R.id.tv_collection_index);
            TextView tvContent = holder.itemView.findViewById(R.id.tv_collection_content);
            if (QuestionType == 0){
                tvYear.setText(mWritings.get(position).getYear());
                tvMonth.setText(mWritings.get(position).getMonth());
                tvIndex.setText(mWritings.get(position).getIndex());
                tvContent.setText(mWritings.get(position).getQuestion());
            }
            if (QuestionType == 1){
                int testPaperIndex = mListenings.get(position).getTestPaperIndex();
                int questionType = mListenings.get(position).getQuestionType();
                TestPaperFactory testPaperFactory = TestPaperFactory.getInstance();
                TestPaperFromWord.TestPaperInfo info = testPaperFactory.getAllTestPaperInfo().get(testPaperIndex);
                String year = info.getYear();
                String month = info.getMonth();
                String index = info.getIndex();
                tvYear.setText(year);
                tvMonth.setText(month);
                tvIndex.setText(index);
                switch (questionType){
                    case 0:
                        tvContent.setText(R.string.listening_short_news);
                        break;
                    case 1:
                        tvContent.setText(R.string.listening_conversation);
                        break;
                    case 2:
                        tvContent.setText(R.string.listening_passages);
                        break;
                }
            }
            if (QuestionType == 2){
                int testPaperIndex = mReadings.get(position).getTestPaperIndex();
                int questionType = mReadings.get(position).getQuestionType();
                TestPaperFactory testPaperFactory = TestPaperFactory.getInstance();
                TestPaperFromWord.TestPaperInfo info = testPaperFactory.getAllTestPaperInfo().get(testPaperIndex);
                String year = info.getYear();
                String month = info.getMonth();
                String index = info.getIndex();
                tvYear.setText(year);
                tvMonth.setText(month);
                tvIndex.setText(index);
                switch (questionType){
                    case 0:
                        tvContent.setText(R.string.reading_choose_word);
                        break;
                    case 1:
                        tvContent.setText(R.string.reading_quickly);
                        break;
                    case 2:
                        tvContent.setText(R.string.reading_carefully);
                        break;
                }
            }
            if (QuestionType == 3){
                tvYear.setText(mTranslations.get(position).getYear());
                tvMonth.setText(mTranslations.get(position).getMonth());
                tvIndex.setText(mTranslations.get(position).getIndex());
                tvContent.setText(mTranslations.get(position).getQuestion());
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"第"+position+"项",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size()>0?mList.size():1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
