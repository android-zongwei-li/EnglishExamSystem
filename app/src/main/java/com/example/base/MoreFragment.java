package com.example.base;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.beans.TestPaper;
import com.example.beans.TestPaperManager;
import com.example.myapplication.R;

import java.util.List;

public class MoreFragment extends LazyFragment {

    public static final String INTENT_INT_INDEX = "index";

    TestPaper testPaper;
    List<TestPaper.ListeningBean.NewsBean> newsBeanList;
    List<TestPaper.ListeningBean.NewsBean.QuestionsBean> questionsBeanList;
    List<String> answers;

    // 用来存储答案序号
    private String answerNos[] = {"A. ","B. ","C. ","D. "};

    // 题目，点击后弹出答案
    private LinearLayout ll_question_1;
    private LinearLayout ll_question_2;
    private LinearLayout ll_question_3;

    // 显示答案的部分
    private RadioGroup rg_answer_1;
    private RadioGroup rg_answer_2;
    private RadioGroup rg_answer_3;

    // 每个题默认不显示答案
    private boolean isAnswerVisible_1 = false;
    private boolean isAnswerVisible_2 = false;
    private boolean isAnswerVisible_3 = false;

    private RadioButton rb_answer;

    // RadioButton的监听，用于记录所选的答案
    MoreFragment.MyOnCheckedChangeListener m1;
    MoreFragment.MyOnCheckedChangeListener m2;
    MoreFragment.MyOnCheckedChangeListener m3;

    public static MoreFragment newInstance(int tabIndex,boolean isLazyLoad) {

        Bundle args = new Bundle();
        args.putInt(INTENT_INT_INDEX, tabIndex);
        args.putBoolean(LazyFragment.INTENT_BOOLEAN_LAZYLOAD, isLazyLoad);
        MoreFragment fragment = new MoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_exam_module);
        View view = View.inflate(getContext(),R.layout.fragment_exam_module,null);
        initData();
        initView(view);

    }

    private void initData(){

        TestPaperManager testPaperManager = TestPaperManager.getInstance();
        testPaper = testPaperManager.getAllTestPaper().get(0);

        newsBeanList = testPaperManager.getAllNewsBeans().get(0);
        questionsBeanList = newsBeanList.get(0).getQuestions();

/*
        // 初始化构造SimplAudioPlayer所需的资源
        uriString = Integer.parseInt(getString(R.string.uriString),R.raw.cet4_201806_01);
*/

    }

    /**
     * 初始化视图
     */
    private void initView(View view){

        //
        ll_question_1 = view.findViewById(R.id.ll_question_1);
        ll_question_2 = view.findViewById(R.id.ll_question_2);
        ll_question_3 = view.findViewById(R.id.ll_question_3);

        rg_answer_1 = view.findViewById(R.id.rg_answer1);
        rg_answer_2 = view.findViewById(R.id.rg_answer2);
        rg_answer_3 = view.findViewById(R.id.rg_answer3);

//        iv_pause = findViewById(R.id.iv_pause);

        //
        ll_question_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isAnswerVisible_1){

                    ll_question_1.findViewById(R.id.rg_answer1).setVisibility(View.VISIBLE);

                    isAnswerVisible_1 = true;

                }else {

                    ll_question_1.findViewById(R.id.rg_answer1).setVisibility(View.GONE);

                    isAnswerVisible_1 = false;
                }

            }
        });
        ll_question_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isAnswerVisible_2){

                    ll_question_2.findViewById(R.id.rg_answer2).setVisibility(View.VISIBLE);

                    isAnswerVisible_2 = true;

                }else {

                    ll_question_2.findViewById(R.id.rg_answer2).setVisibility(View.GONE);

                    isAnswerVisible_2 = false;
                }

            }
        });
        ll_question_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isAnswerVisible_3){

                    ll_question_3.findViewById(R.id.rg_answer3).setVisibility(View.VISIBLE);

                    isAnswerVisible_3 = true;

                }else {

                    rg_answer_3.setVisibility(View.GONE);

                    isAnswerVisible_3 = false;
                }

            }
        });

        setAnswer();

        m1 = new MoreFragment.MyOnCheckedChangeListener();
        m2 = new MoreFragment.MyOnCheckedChangeListener();
        m3 = new MoreFragment.MyOnCheckedChangeListener();
        rg_answer_1.setOnCheckedChangeListener(m1);
        rg_answer_2.setOnCheckedChangeListener(m2);
        rg_answer_3.setOnCheckedChangeListener(m3);

    }

    /**
     * 设置每道题目的答案,作为初始视图的一部分
     * 后面看能不能优化一下，传入list参数，以使此方法更加通用
     */
    private void setAnswer(){
        //外层设置每个题对应的答案,每次循环设置一道题目
        for (int i = 0; i < questionsBeanList.size(); i++){
            answers = questionsBeanList.get(i).getAnswers();

            // 如果只有两个题目，把第三个隐藏了
            if (questionsBeanList.size() < 3){
                ll_question_3.setVisibility(View.GONE);
            }

            if (i == 0){
                //内层设置每个选项的对应的答案
                for(int j = 0; j < 4; j++){
                    rb_answer = (RadioButton) rg_answer_1.getChildAt(j);
                    rb_answer.setText(answerNos[j]+answers.get(j));
                }
            }
            if (i == 1){
                for(int j = 0; j < 4; j++){
                    rb_answer = (RadioButton) rg_answer_2.getChildAt(j);
                    rb_answer.setText(answerNos[j]+answers.get(j));
                }
            }
            if (i == 2){
                for(int j = 0; j < 4; j++){
                    rb_answer = (RadioButton) rg_answer_3.getChildAt(j);
                    rb_answer.setText(answerNos[j]+answers.get(j));
                }
            }

        }
    }

    /**
     * RadioButton的监听，用于记录所选择的答案
     */
    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{
        String selected;

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case 0 :
                    selected = "A";
                    break;
                case 1 :
                    selected = "B";
                    break;
                case 2 :
                    selected = "C";
                    break;
                case 3 :
                    selected = "D";
                    break;
            }
        }
    }

}