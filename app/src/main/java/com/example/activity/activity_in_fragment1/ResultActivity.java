package com.example.activity.activity_in_fragment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.MainActivity;
import com.example.activity.base.BaseAppCompatActivity;
import com.example.beans.MySqliteManager;
import com.example.beans.Word;
import com.example.myapplication.R;
import com.example.utils.AccountManager;
import com.example.utils.LogUtils;
import com.example.utils.MySqlDBOpenHelper;
import com.example.view.topbar.TopBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这个类用于显示考试结果，以及结果分析、给出建议
 */
public class ResultActivity extends BaseAppCompatActivity {

    // 需要上一个页面传递过来的数据
    public static final String TYPE = "type";  //记录是翻译还是写作
    public static final String SCORE = "score";//分数
    public static final String WORDSNUM = "wordsNum";//单词数
    public static final String WORDSERRORNUM = "wordsErrorNum";//错误单词数
    public static final String SUBMITNUM = "submitNum";// 提交次数
    public static final String CONTENT = "content";// 用户写的作文
    public static final String USERTIME = "userTime";// 用时
    public static final String ANSWER = MySqliteManager.Writing.QUESTION;//范文

    private String mType; //记录是翻译还是写作

    double score;//分数
    int wd;//单词数
    int sentence;//句子数
    int errorWords;//错误单词数
    int submitNum;//提交次数
    long userTime;
    String content;
    String answer;

    private int title_type;

    String advice;//点评

    private List<String> words = new ArrayList<>();

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
        mType = intent.getStringExtra(TYPE);

        if ( mType.equals("translation") ){
            title_type = R.string.title_translation_result;
        }else {
            title_type = R.string.title_writing_result;
        }
    }

    private void initView() {
        //
        TopBar topBar = findViewById(R.id.topBar);
        topBar.setTitle(title_type);
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

        TextView tv_actual_score = findViewById(R.id.tv_actual_score);//分数
        TextView tv_word_number = findViewById(R.id.tv_word_number);//单词数
        TextView tv_error_word_num = findViewById(R.id.tv_error_word_num);//错误单词数
        TextView tv_submit_num = findViewById(R.id.tv_submit_num);//提交次数
        TextView tv_writing_advice = findViewById(R.id.tv_writing_advice);//作文点评
        TextView tv_writing_content = findViewById(R.id.tv_writing_content);//作文内容
        TextView tv_user_time = findViewById(R.id.tv_user_time);//用时
        Chronometer chronometer = findViewById(R.id.result_chronometer);
        TextView tv_writing_answer = findViewById(R.id.tv_writing_answer);

        TextView tvContent = findViewById(R.id.tv_result_content);
        TextView tvComments = findViewById(R.id.tv_result_comments);
        TextView tvReference = findViewById(R.id.tv_result_reference);
        if ( mType.equals("translation") ){
            tvContent.setText(R.string.my_translation);
            tvComments.setText(R.string.translation_comment);
            tvReference.setText(R.string.translation_reference);
        }


        // 单词总数
        wd = wordSum(content);
        tv_word_number.setText(getString(R.string.total_words) + wd);
        // 错误单词
        tv_error_word_num.setText(getString(R.string.wrong_words) + errorWords);
        // 提交次数
        tv_submit_num.setText(getString(R.string.submit_num) + submitNum);

       /*
       错误记录：
       R.string.xxx取的是id，再 + 一个 int值，会导致返回的字符串不是预期的。
        tv_submit_num.setText(R.string.submit_num + submitNum);
*/

        //用时,记录第一次提交所用的时间
        if (submitNum == 1){
            chronometer.setBase(userTime);
        }


        // 作文：在结果页面标识出错误单词
        tv_writing_content.setText(content);

        // 点评
        if (isCapital(content) == false){
            advice = "建议首字母大写；\n";
            score = score - 5;
        }
        if (wd < 80){
            advice += "文章篇幅太短，未达基本词汇要求；\n";
        }
        if (errorWords > 5){
            advice += "单词拼写错误较多，建议多背单词；\n";
        }
        tv_writing_advice.setText(advice.trim());

        // 根据用时做一个点评

        // 错误单词判断，先下载一个单词库，在其中查找，没找到就去联网查找，没找到，就判断为错误单词


        // 范文
        tv_writing_answer.setText(answer);

        // 分数
        tv_actual_score.setText(getString(R.string.score) + score + "");

        // 获取文章中所有的单词
        wordsNumber(content);

        //返回主页
        TextView tv_back_home = findViewById(R.id.tv_back_home);
        tv_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, MainActivity.class));
            }
        });
    }

    /**
     * 统计一篇文章(一个字符串)中的单词总数
     * @param content
     * @return
     */
    private int wordSum(String content){
        int word = 0;
        for (int i = 0; i < content.length(); i++){
            char c = content.charAt(i);

            boolean isLetter = c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
            boolean isLast = i == (content.length()-1);
            //判断是不是最后一个字符，若是，则判断最后一个字符是不是英文字母
            if (isLast && isLetter){
                word++;
            }

            //判断字符是否为a~z,A~Z,若是，继续循环，否则word+1
            if(isLetter) {
                continue;
            }else {
                word++;
            }

        }
        return word;
    }

    /**
     * 判断首字母是否大写
     * @return
     */
    private boolean isCapital(String content){

        //判断字符串是否为空或者长度为0
        if (content == null || content.length() <= 0) {
            return false;
        }

        char c = content.charAt(0);
        boolean isCapital = c >= 'A' && c <= 'Z';

        return isCapital;

    }

    /**
     * 获取文章中所有的单词,并把单词添加到words集合中
     * @param content
     */
    private void wordsNumber(String content){
        String s = "\\d+.\\d+|\\w+";
        Pattern p = Pattern.compile(s);
        Matcher m = p.matcher(content);

        while (m.find()){
         //   LogUtils.v("单词预览",m.group());
            words.add(m.group());
        }

    }


}
