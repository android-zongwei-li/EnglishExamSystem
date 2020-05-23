package com.example.activity.acticity_in_fragment1.listening;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.activity.base.BaseAppCompatActivity;
import com.example.base.MoreFragment;
import com.example.beans.TestPaper;
import com.example.beans.TestPaperManager;
import com.example.myapplication.R;
import com.example.view.SimpleAudioPlayer;
import com.example.view.topbar.TopBar;

import java.util.ArrayList;
import java.util.List;

public class ShortNewsActivity extends BaseAppCompatActivity {

    TestPaper testPaper;
    List<TestPaper.ListeningBean.NewsBean> newsBeanList;
    List<TestPaper.ListeningBean.NewsBean.QuestionsBean> questionsBeanList;
    List<String> answers;

    private TopBar topBar;

    ViewPager viewPager;
    private List<Fragment> fragmentList;

    private SimpleAudioPlayer simpleAudioPlayer;

/*
    // 听力播放资源uri
    int uriString;
*/

    // 显示试卷信息
    private TextView tv_test_paper_name;

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

    // 用来存储答案序号
    private String answerNos[] = {"A. ","B. ","C. ","D. "};

    //
    private List<String> selectedAnswer;

    // 听力正确答案
    private String listeningCorrectAnswer[] = {
            "A","B","B","C","A",
            "C","D","A","D","C",
            "B","D","C","A","D",
            "B","A","C","D","B",
            "A","B","D","C","B"};

    // 总分数 710
    private double totalScore = 0;
    // 听力 248.5
    private double listeningScore = 0;
    // 阅读 248.5
    private double readingScore = 0;
    // 翻译 106.5
    private double transScore = 0;
    // 写作 106.5
    private double writingScore = 0;

    // RadioButton的监听，用于记录所选的答案
    MyOnCheckedChangeListener m1;
    MyOnCheckedChangeListener m2;
    MyOnCheckedChangeListener m3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_news);

        viewPager = findViewById(R.id.viewPager);
        fragmentList = new ArrayList<>();
        fragmentList.add(MoreFragment.newInstance(1,false));
        fragmentList.add(MoreFragment.newInstance(2,true));
        fragmentList.add(MoreFragment.newInstance(3,true));
        fragmentList.add(MoreFragment.newInstance(4,true));

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(supportFragmentManager, fragmentList);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(4);

        initData();

        initView();

       /* //权限判断，如果没有权限就请求权限
        if (ContextCompat.checkSelfPermission(ShortNewsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ShortNewsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
        }*/

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
    private void initView(){

        //
        simpleAudioPlayer = findViewById(R.id.simple_audio_player);

        //
        topBar = findViewById(R.id.topBar);
        topBar.setTitle("短篇新闻");
        topBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                simpleAudioPlayer.finishPlay();
                finish();
            }

            // 这里是交卷按钮的监听
            @Override
            public void rightClick() {
                // 交卷之前做一下是否有题未完成的判断
                if (rg_answer_1.getCheckedRadioButtonId() == -1){
                    Toast.makeText(getBaseContext(),"第一题还未作答",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rg_answer_2.getCheckedRadioButtonId() == -1){
                    Toast.makeText(getBaseContext(),"第二题还未作答",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ll_question_3.getVisibility() == View.VISIBLE & rg_answer_3.getCheckedRadioButtonId() == -1){
                    Toast.makeText(getBaseContext(),"第三题还未作答",Toast.LENGTH_SHORT).show();
                    return;
                }

                // 全部做完，把每个题目的答案保存下来
                selectedAnswer.add(m1.selected);
                selectedAnswer.add(m2.selected);
                selectedAnswer.add(m3.selected);

                // 试卷批阅，对比所选答案和正确答案
                for (int i = 0; i < selectedAnswer.size(); i++){
                    if (selectedAnswer.get(i) == listeningCorrectAnswer[i]){
                        listeningScore += 6;
                    }
                }
                totalScore = listeningScore + readingScore + transScore + writingScore;
                Toast.makeText(getBaseContext(),"分数为: "+totalScore,Toast.LENGTH_SHORT).show();
                return;
            }
        });

        //
        tv_test_paper_name = findViewById(R.id.tv_test_paper_name);
        tv_test_paper_name.setText(testPaper.getName());


        View view = View.inflate(this,R.layout.fragment_exam_module,null);
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

        m1 = new MyOnCheckedChangeListener();
        m2 = new MyOnCheckedChangeListener();
        m3 = new MyOnCheckedChangeListener();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                }else{
                    Toast.makeText(this, "拒绝权限，将无法使用程序。", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 在这销毁资源总会有延迟，该如何解决?
        simpleAudioPlayer.finishPlay();

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

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

}