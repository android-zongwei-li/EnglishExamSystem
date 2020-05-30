package com.example.activity.acticity_in_fragment1.reading;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.example.beans.Question;
import com.example.myapplication.R;
import com.example.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by: lzw.
 * Date: 2020/5/28
 * Time: 16:30
 * Description：
 * 1、
 */
public class QuicklyRVAdapter extends RecyclerView.Adapter {
    private int index = 2;
    List<Question> quicklyReadingQuestions; // 快速阅读的问题
    // 快速阅读的段落序号
    List<String> passageLetterss;
    private List<String> rightChoicess;
    //记录快速阅读选择的答案
    private Map<Integer,Integer> selectedItems = new HashMap<>();
    private List<String> carefullyReadingUserAnswers;

    public QuicklyRVAdapter(int i,List<Question> questionList,List<String> stringList,List<String> rightChoices){
        index = i;
        //初始界面
        if (i == 0){
            LogUtils.i("哪个适配器","前");
            quicklyReadingQuestions = questionList;
            passageLetterss= stringList;
            rightChoicess = rightChoices;
        } else {//交卷后的界面
            LogUtils.i("哪个适配器","后");
            quicklyReadingQuestions = questionList;
            carefullyReadingUserAnswers = stringList;
            rightChoicess = rightChoices;
        }

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (index == 0){
            LogUtils.i("哪个适配器","前");
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.drawer_content_list_item,parent,false);
        }else {
            LogUtils.i("哪个适配器","后");

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.drawer_content_list_item_result,parent,false);
        }
        MyHolder holder = new MyHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        TextView textView = holder.itemView.findViewById(R.id.tv_drawer_content_item);
        textView.setText(quicklyReadingQuestions.get(position).getQuestion().trim());
        if (index == 0){
            LogUtils.i("哪个适配器","前");

            WheelPicker wheel = holder.itemView.findViewById(R.id.wheel);
            wheel.setData(passageLetterss);
            wheel.setIndicator(true);
            wheel.setIndicatorColor(R.color.exit_dialog_orange);
            wheel.setVisibleItemCount(3);
            wheel.setItemTextColor(R.color.colorBlack);
            wheel.setSelectedItemTextColor(R.color.colorAccent);
            wheel.setCurtain(true);
            wheel.setAtmospheric(true);
            wheel.isCurved();

            wheel.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker picker, Object data, int p) {
                    //position表示题号，p为选择的答案
                    selectedItems.put(position,p);
                }
            });

        }else {
            LogUtils.i("哪个适配器","后");

            LinearLayout ll = holder.itemView.findViewById(R.id.ll_carefully_reading_result1);
            ll.setVisibility(View.VISIBLE);
            TextView tv_right_answer = ll.findViewById(R.id.tv_carefully_correct_answer);
            tv_right_answer.setText("正确答案："+rightChoicess.get(position));
            TextView tv_user_answer = ll.findViewById(R.id.tv_user_answer);
            tv_user_answer.setText("你的答案："+carefullyReadingUserAnswers.get(position));
        }


/*        TextView tv_user_answer = holder.itemView.findViewById(R.id.tv_user_answer);
        userAnswerTV.add(tv_user_answer);*/
    }

    @Override
    public int getItemCount() {
        return quicklyReadingQuestions.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        public MyHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public Map<Integer,Integer> getSelectedItems(){
        return selectedItems;
    }
}
