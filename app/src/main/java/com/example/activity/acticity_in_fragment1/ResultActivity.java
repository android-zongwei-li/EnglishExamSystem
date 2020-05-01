package com.example.activity.acticity_in_fragment1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.activity.base.BaseAppCompatActivity;
import com.example.beans.MySqliteManager;
import com.example.myapplication.R;
import com.example.view.topbar.TopBar;

/**
 * 这个类用于显示考试结果，以及结果分析、给出建议
 */
public class ResultActivity extends BaseAppCompatActivity {

    // 需要上一个页面传递过来的数据
    public static final String SCORE = "score";//分数
    public static final String WORDSNUM = "wordsNum";//单词数
    public static final String WORDSERRORNUM = "wordsErrorNum";//错误单词数
    public static final String SUBMITNUM = "submitNum";// 提交次数
    public static final String CONTENT = "content";// 用户写的作文
    public static final String USERTIME = "userTime";// 用时
    public static final String ANSWER = MySqliteManager.Writing.QUESTION;//范文


    double score;//分数
    int wd;//单词数
    int sentence;//句子数
    int errorWords;//错误单词数
    int submitNum;//提交次数
    long userTime;
    String content;
    String answer;

    TopBar topBar;

    TextView tv_actual_score;//分数
    TextView tv_word_number;//单词数
    TextView tv_error_word_num;//错误单词数
    TextView tv_submit_num;//提交次数
    TextView tv_writing_content;//作文内容
    TextView tv_writing_advice;//作文点评
    TextView tv_user_time;//用时
    Chronometer chronometer;
    TextView tv_writing_answer;

    String advice;//点评

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initData();
        initView();
    }

    private void initData(){
        Intent intent = getIntent();
        score = intent.getDoubleExtra(SCORE,0);
        wd = intent.getIntExtra(WORDSNUM,0);
        errorWords = intent.getIntExtra(WORDSERRORNUM,0);
        submitNum = intent.getIntExtra(SUBMITNUM,0);
        content = intent.getStringExtra(CONTENT);
        userTime = intent.getLongExtra(USERTIME,0);
        answer = intent.getStringExtra(ANSWER);
    }

    private void initView() {
        //
        topBar = findViewById(R.id.topBar);
        topBar.setTitle("作文详情");
        topBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
                finish();
            }

            @Override
            public void rightClick() {
                finish();//返回到写作页面，继续更改
            }
        });

        tv_actual_score = findViewById(R.id.tv_actual_score);
        tv_word_number = findViewById(R.id.tv_word_number);
        tv_error_word_num = findViewById(R.id.tv_error_word_num);
        tv_submit_num = findViewById(R.id.tv_submit_num);
        tv_writing_advice = findViewById(R.id.tv_writing_advice);
        tv_writing_content = findViewById(R.id.tv_writing_content);
        tv_user_time = findViewById(R.id.tv_user_time);
        chronometer = findViewById(R.id.result_chronometer);
        tv_writing_answer = findViewById(R.id.tv_writing_answer);

        // 分数
        tv_actual_score.setText("分数: "+score);
        // 单词总数
        tv_word_number.setText("单词总数: "+wd);
        // 错误单词
        tv_error_word_num.setText("错误单词: "+errorWords);
        // 提交次数
        tv_submit_num.setText("提交次数: "+submitNum);
        //用时,记录第一次提交所用的时间
        if (submitNum == 1){
            chronometer.setBase(userTime);
        }


        // 作文：在结果页面标识出错误单词
        tv_writing_content.setText(content);

        // 点评
        if (wd < 80){
            advice = "文章篇幅太短，未达基本词汇要求；";
        }
        if (errorWords > 5){
            advice += "/n单词拼写错误较多，建议多背单词；";
        }
        tv_writing_advice.setText(advice);

        // 根据用时做一个点评


        // 错误单词判断，先下载一个单词库，在其中查找，没找到就去联网查找，没找到，就判断为错误单词


        // 范文
        tv_writing_answer.setText(answer);
    }


}
