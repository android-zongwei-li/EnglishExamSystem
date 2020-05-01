package com.example.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.beans.Question;
import com.example.myapplication.R;
import com.example.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description：
 */
public class CarefullyReadingFragment extends Fragment {

    private String title;
    private List<Question> carefullyReadingQuestions;
    private List<String> choices = new ArrayList<>();

    private LinearLayout ll_question;
    TextView tv_question_index;
    List<TextView> questionsIndexTextViews = new ArrayList<>();

    private int questionsIndex; //记录题号
    public Map questionMap = new ArrayMap();

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull final Message msg) {

            if (ll_question.getVisibility() == ll_question.GONE){
                ll_question.setVisibility(View.VISIBLE);
            }else if (questionsIndex == msg.what){    //两次点击相同textView
                ll_question.setVisibility(View.GONE);
            }
            questionsIndex = msg.what;

            TextView textView = (TextView) ll_question.getChildAt(0);
            textView.setText(carefullyReadingQuestions.get(msg.what).getQuestion().trim());

            choices.clear();
            choices.add(carefullyReadingQuestions.get(msg.what).getChoiceA().trim());
            choices.add(carefullyReadingQuestions.get(msg.what).getChoiceB().trim());
            choices.add(carefullyReadingQuestions.get(msg.what).getChoiceC().trim());
            choices.add(carefullyReadingQuestions.get(msg.what).getChoiceD().trim());

            final RadioGroup rg = (RadioGroup) ll_question.getChildAt(1);
            if (questionMap.containsKey(questionsIndex)){   //当前题目存在，还原答案
                rg.check((Integer) questionMap.get(questionsIndex));
            }else { // 题目还没做
                rg.clearCheck();
            }
            for (int i = 0; i < 4; i++){
                RadioButton rb = (RadioButton) rg.getChildAt(i);
                rb.setText(choices.get(i));
            }
            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    questionMap.put(questionsIndex,(rg.getCheckedRadioButtonId())%4);  //把当前的题号和题目存入map中
                    LogUtils.i("questionsMap",questionMap.toString());
                    questionsIndexTextViews.get(questionsIndex)
                            .setTextColor(getResources().getColor(R.color.text_finish));
                }
            });

        }
    };

    public CarefullyReadingFragment(String title, List<Question> carefullyReadingQuestions) {
        this.title = title;
        this.carefullyReadingQuestions = carefullyReadingQuestions;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carefully_reading_exam,null);
        TextView tv_carefully_reading_title = view.findViewById(R.id.tv_carefully_reading_title);
        ll_question = view.findViewById(R.id.ll_question);
        tv_carefully_reading_title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ll_question.setVisibility(View.GONE);
                return false;
            }
        });
        tv_carefully_reading_title.setText(title.trim());

        LinearLayout ll_question_index = view.findViewById(R.id.ll_question_index);
        for (int i = 0; i < 5; i++){
            tv_question_index = (TextView) ll_question_index.getChildAt(i);
            questionsIndexTextViews.add(tv_question_index);
            final int finalI = i;
            tv_question_index.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.sendEmptyMessage(finalI);
                }
            });
        }

        return view;
    }

}
