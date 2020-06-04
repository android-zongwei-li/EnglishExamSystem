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
import com.example.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description：
 */
public class CarefullyReadingFragment extends Fragment {

    private String title;   //文章内容
    private List<Question> carefullyReadingQuestions;   //问题(Question)集合
    public CarefullyReadingFragment(String title, List<Question> carefullyReadingQuestions) {
        this.title = title;
        this.carefullyReadingQuestions = carefullyReadingQuestions;
    }

    public List<TextView> questionsIndexTextViews = new ArrayList<>();
    //显示题目和选项
    private LinearLayout ll_question;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carefully_reading_exam,null);
        //显示文章内容
        final TextView tv_carefully_reading_title = view.findViewById(R.id.tv_carefully_reading_title);
        ll_question = view.findViewById(R.id.ll_question);
        tv_carefully_reading_title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ll_question.setVisibility(View.GONE);

                // 情形一，看下面的解析，这种情况也能触发onClick事件。
              //  tv_carefully_reading_title.performClick();

                return true;
            }
        });
        // 情形二，onTouch返回true时，事件已经被消费，在这里使用此句就不再会触发onClick了
        // tv_carefully_reading_title.performClick();
        /**
         * 没有下面一行代码会出现
         * warning：
         *  Custom view “TextView” has ’setOnTouchListener‘ called on it but does not override 'performClick'
         *  'onTouch' should call 'View#performClick' when a click is detected
         *  意思是可能和点击事件发生冲突。
         *
         * 在onTouch中返回true,同时又添加了onClick监听,这时onClick就不会执行了,
         * 事件被onTouch消化掉了.来查看一下执行顺序就知道了,
         * onTouchEvent=>performClick=>onClick,所以在onTouch返回true时,同时又添加了onClick监听,
         * 此时在onTouch中适当的地方执行performClick方法,来触发onClick.
         *
         * 那onTouch返回true的情况下，就不能触发onClick了吗？
         * 也不是，看onTouch中的 “情形一”，此时也会触发onClick。
         *
         * 简单的结论就是：
         * onTouch，返回true：
         *      默认点击不触发onClick，可加入“情形一”来触发。
         *
         * onTouch，返回false：
         *      不仅触发onTouch，还会触发onClick
         *
         */
        tv_carefully_reading_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(getActivity(),"onClick触发了");
            }
        });
        tv_carefully_reading_title.setText(title.trim());


        //显示题号:Q1-Q5。点击后，显示题目和选项。
        LinearLayout ll_question_index = view.findViewById(R.id.ll_question_index);
        for (int i = 0; i < 5; i++){
            TextView tv_question_index = (TextView) ll_question_index.getChildAt(i);
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

    public RadioGroup rg;
    private List<String> choices = new ArrayList<>();
    public Map questionMap = new ArrayMap();
    private int questionsIndex; //记录题号
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull final Message msg) {

            if (ll_question.getVisibility() == View.GONE){
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

            rg = (RadioGroup) ll_question.getChildAt(1);

            LogUtils.i("RadioGroup",rg.toString());
            for (int i = 0; i < 4; i++){
                RadioButton rb = (RadioButton) rg.getChildAt(i);
                rb.setText(choices.get(i));
                rb.setId(i);
            }

            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    if (checkedId != -1){
                        //把当前的题号和题目存入map中
                        questionMap.put(questionsIndex,(rg.getCheckedRadioButtonId())%4);
                        LogUtils.i("questionsMap",questionMap.toString());
                        questionsIndexTextViews.get(questionsIndex)
                                .setTextColor(getResources().getColor(R.color.text_finish));
                    }

                    if (checkedId == -1){
                        questionsIndexTextViews.get(questionsIndex)
                                .setTextColor(getResources().getColor(R.color.default_text));
                    }

                }
            });

            if (questionMap.size() != 0){
                if (questionMap.containsKey(questionsIndex)){
                    rg.check((Integer) questionMap.get(questionsIndex));
                }else {
                    rg.check(-1);
                    questionMap.remove(questionsIndex);
                    LogUtils.i("questionsMap",questionMap.toString());
                }
            }

        }
    };

}
