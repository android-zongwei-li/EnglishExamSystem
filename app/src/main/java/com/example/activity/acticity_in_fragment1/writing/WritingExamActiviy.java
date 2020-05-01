package com.example.activity.acticity_in_fragment1.writing;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.activity.acticity_in_fragment1.ResultActivity;
import com.example.activity.base.BaseAppCompatActivity;
import com.example.beans.CollectionBank;
import com.example.beans.MySqliteManager;
import com.example.beans.Writing;
import com.example.myapplication.R;
import com.example.utils.LogUtils;
import com.example.view.CommonControlBar;
import com.example.view.topbar.TopBar;

public class WritingExamActiviy extends BaseAppCompatActivity {

    private final String title = "写作";

    // 需要上一个页面传递过来的数据
    public static final String QUESTION = MySqliteManager.Writing.QUESTION;//作文题目
    public static final String ANSWER = MySqliteManager.Writing.ANSWER;//范文，要传递给下一个页面
    public static final String YEAR = MySqliteManager.Writing.YEAR;// 年份
    public static final String MONTH = MySqliteManager.Writing.MONTH;// 月份
    public static final String INDEX = MySqliteManager.Writing.INDEX;// 第几套，这三个用来显示题目来源
    public static final String QUESTIONID = MySqliteManager.Writing.QUESTIONID;// 这个数据的标识，收藏需要用到

    // 作文题目
    private Writing mWriting;
    private String writingTitle;
    private String answer;    // 范文
    private String year;
    private String month;
    private String index;
    private String questionID;

    double score;//分数
    int wd;//单词数
    int sentence;//句子数
    int errorWords;//错误单词数
    int submitNum;//提交次数

    TopBar topBar;
    CommonControlBar commonControlBar;
    Chronometer chronometer;
    TextView tv_testpaper_name;//试卷名称
    TextView tv_writing_title;//作文题目
    EditText ed_writing_content;
    TextView getTv_writing_title_set;

    // 输入的作文
    String content;

    // 记录是否已经收藏
    private boolean isCollected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_exam);

        initData();

        initView();
    }

    private void initData() {

        Intent intent = getIntent();
        writingTitle = intent.getStringExtra(QUESTION);
        answer = intent.getStringExtra(ANSWER);
        year = intent.getStringExtra(YEAR);
        month = intent.getStringExtra(MONTH);
        index = intent.getStringExtra(INDEX);
        questionID = intent.getStringExtra(QUESTIONID);

        mWriting = (Writing) intent.getSerializableExtra("writingBean");
    }

    private void initView() {

        // 初始化 topbar
        topBar = findViewById(R.id.topBar);
        topBar.setTitle(title);
        topBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            // 交卷按钮的事件处理
            @Override
            public void rightClick() {
                content = ed_writing_content.getText().toString().trim();
                if (content.length() == 0){
                    Toast.makeText(getBaseContext(),"无内容，请完成作文",Toast.LENGTH_SHORT).show();
                }else{
                    // 作文批阅
                    getWritingResult(content);
                    Intent intent = new Intent(getBaseContext(), ResultActivity.class);
                    intent.putExtra(ResultActivity.SCORE,score);
                    intent.putExtra(ResultActivity.WORDSNUM,wd);
                    intent.putExtra(ResultActivity.WORDSERRORNUM,errorWords);
                    intent.putExtra(ResultActivity.SUBMITNUM,++submitNum);
                    intent.putExtra(ResultActivity.CONTENT,content);
                    intent.putExtra(ResultActivity.USERTIME,chronometer.getBase());
                    intent.putExtra(ResultActivity.ANSWER,answer);
                    startActivity(intent);
                }
            }

        });

        //
        commonControlBar = findViewById(R.id.commonControlBar);
        chronometer = commonControlBar.findViewById(R.id.chronometer);
        final ImageView ivIConCollection = commonControlBar.findViewById(R.id.iv_icon_collection);
        ivIConCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionBank collectionBank = CollectionBank.getInstance();
                if (!isCollected){    //默认为false，为收藏
                    isCollected = true;
                    ivIConCollection.setImageResource(R.drawable.icon_collection_after);
                    collectionBank.add(mWriting);
                    Toast.makeText(WritingExamActiviy.this,"已收藏",Toast.LENGTH_SHORT).show();
                    LogUtils.i("collectedWriting",collectionBank.getCollectedWritings().toString());
                }else {
                    isCollected = false;
                    ivIConCollection.setImageResource(R.drawable.icon_collection_befor);
                    collectionBank.remove(mWriting);
                    Toast.makeText(WritingExamActiviy.this,"取消收藏",Toast.LENGTH_SHORT).show();
                    LogUtils.i("collectedWriting",collectionBank.getCollectedWritings().toString());
                }
            }
        });

        //
        tv_testpaper_name = findViewById(R.id.tv_test_paper_name);
        tv_testpaper_name.setText(year+"年"+month+"月 第"+index+"套");

        // 题目设置
        tv_writing_title = findViewById(R.id.tv_writing_title);
        tv_writing_title.setText(writingTitle);

        //用于替换题目
        getTv_writing_title_set = findViewById(R.id.tv_writing_set);
        getTv_writing_title_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTv_writing_title_set.setVisibility(View.GONE);
                tv_writing_title.setVisibility(View.VISIBLE);
            }
        });

        // 写作模块
        ed_writing_content = findViewById(R.id.ed_writing_content);
        ed_writing_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               tv_writing_title.setVisibility(View.GONE);
               getTv_writing_title_set.setVisibility(View.VISIBLE);
            }
        });
        ed_writing_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_writing_title.setVisibility(View.GONE);
                getTv_writing_title_set.setVisibility(View.VISIBLE);
                if (s.length() == 0){
                    getTv_writing_title_set.setVisibility(View.GONE);
                    tv_writing_title.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 处理作文，准备评分所需要的一些数据，交卷后传给ResultActivity
     * @param content
     */
    private void getWritingResult(String content) {
        wd = 0;
        errorWords = 0;
        score = 106.5;

        // 统计单词数
        for (int i = 0; i < content.length(); i++){
            char c = content.charAt(i);
            if (c == ' '|| c == '.' || c == '!' || c == '?'|| c == '。' || c == '！' || c == '？'|| c == ','|| c == '，'|| c == '：'|| c == ':'|| c == ';'|| c == '；'){
                wd++;
            }
            /*if (c == '.' || c == '!' || c == '?'){
                if (i > 0 && content.charAt(i - 1) != '.' && content.charAt(i -1) != '!' && content.charAt(i -1) != '?'){
                    sentence++;
                }
            }*/
        }
        //对最后一个单词没有结束符号的处理
        char c = 'a';
        for (int i = 0; i < 26; i++){
            if (content.charAt(content.length()-1) == c++){
                wd = wd + 1;
                score = score - 2;
                break;
            }
        }
        //对文章首字母没有大写的处理
        char c1 = 'A';
        for (int i = 0; i < 26; i++){
            if (content.charAt(0) != c1++){
                score = score - 2;
                break;
            }
        }

        // 是否有错词，统计错词数目


        // 分数
        if (wd < 120 || wd > 180){   //单词数给分
            score = score - 10;
        }

    }

}