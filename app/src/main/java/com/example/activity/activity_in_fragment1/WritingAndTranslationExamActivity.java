package com.example.activity.activity_in_fragment1;

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

import com.example.activity.base.BaseAppCompatActivity;
import com.example.beans.MySqliteManager;
import com.example.beans.Translation;
import com.example.beans.Writing;
import com.example.myapplication.R;
import com.example.utils.AccountManager;
import com.example.utils.LogUtils;
import com.example.utils.MySqlDBOpenHelper;
import com.example.utils.ToastUtils;
import com.example.view.CommonControlBar;
import com.example.view.topbar.TopBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class WritingAndTranslationExamActivity extends BaseAppCompatActivity {

    // 需要上一个页面传递过来的数据
    public static final String TYPE = "type";  //记录是翻译还是写作
    public static final String QUESTION = MySqliteManager.Writing.QUESTION;//作文题目
    public static final String ANSWER = MySqliteManager.Writing.ANSWER;//范文，要传递给下一个页面
    public static final String YEAR = MySqliteManager.Writing.YEAR;// 年份
    public static final String MONTH = MySqliteManager.Writing.MONTH;// 月份
    public static final String INDEX = MySqliteManager.Writing.INDEX;// 第几套，这三个用来显示题目来源
    public static final String QUESTIONID = MySqliteManager.Writing.QUESTIONID;// 这个数据的标识，收藏需要用到

    // 作文题目
    private String mType;//题型
    private Writing mWriting;
    private Translation mTranslation;
    private String title;
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

    Chronometer chronometer;
    TextView tv_testpaper_name;//试卷名称
    TextView tv_title;//作文题目
    EditText ed_content;
    TextView tvReplaceTitle;

    // 输入的作文
    String content;

    // 记录是否已经收藏
    private boolean isCollected = false;

    private int title_type;

    //已收藏写作
    private ArrayList<Writing> collectedWriting = new ArrayList<>();
    //已收藏翻译
    private ArrayList<Translation> collectedTranslation = new ArrayList<>();

    private AccountManager am;
    private String telephone;

    private long usedTime;
    CommonControlBar commonControlBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_exam);

        initData();

        initView();
    }

    private void initData() {

        am = AccountManager.getInstance(getApplication());
        telephone = am.getTelephone();
        if (am.isOnline()){
            initCollectedWritingAndTranslation(telephone);
        }

        Intent intent = getIntent();
        title = intent.getStringExtra(QUESTION);
        answer = intent.getStringExtra(ANSWER);
        year = intent.getStringExtra(YEAR);
        month = intent.getStringExtra(MONTH);
        index = intent.getStringExtra(INDEX);
        questionID = intent.getStringExtra(QUESTIONID);
        mType = intent.getStringExtra(TYPE);

        if ( mType.equals("translation") ){
            mTranslation = (Translation) intent.getSerializableExtra("transBean");
            title_type = R.string.title_translation;
        }else {
            mWriting = (Writing) intent.getSerializableExtra("writingBean");
            title_type = R.string.title_writing;
        }

    }

    private void initView() {

        // 初始化 topbar
        TopBar topBar = findViewById(R.id.topBar);
        topBar.setTitle(title_type);
        topBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            // 交卷按钮的事件处理
            @Override
            public void rightClick() {
                content = ed_content.getText().toString().trim();
                if (content.length() == 0){
                    Toast.makeText(getBaseContext(), R.string.writing_and_translation_tips,Toast.LENGTH_SHORT).show();
                }else{
                    // 作文批阅
                    getWritingResult(content);
                    Intent intent = new Intent(getBaseContext(), ResultActivity.class);
                    intent.putExtra(ResultActivity.TYPE,mType);
                    intent.putExtra(ResultActivity.SCORE,score);
                    intent.putExtra(ResultActivity.WORDSNUM,wd);
                    intent.putExtra(ResultActivity.WORDSERRORNUM,errorWords);
                    intent.putExtra(ResultActivity.SUBMITNUM,++submitNum);
                    intent.putExtra(ResultActivity.CONTENT,content);
                    commonControlBar.stop();
                    intent.putExtra(ResultActivity.USERTIME,commonControlBar.getRecordingTime());
                    intent.putExtra(ResultActivity.ANSWER,answer);
                    startActivity(intent);
                }
            }

        });

        //
        commonControlBar = findViewById(R.id.commonControlBar);
        final ImageView ivIConCollection = commonControlBar.findViewById(R.id.iv_icon_collection);
        ivIConCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (am.isOnline()){

                    if (!isCollected){    //默认为false，为收藏
                        isCollected = true;
                        ivIConCollection.setImageResource(R.drawable.icon_collection_after);
                        if (mType.equals("translation")) {
                            if (!collectedTranslation.contains(mTranslation)){
                                collectedTranslation.add(mTranslation);
                            }
                        } else {
                            if (!collectedWriting.contains(mWriting)){
                                collectedWriting.add(mWriting);
                            }
                            //        collectionBank.add(mWriting);
                            //       LogUtils.i("collectedWriting",collectionBank.getCollectedWritings().toString());
                        }
                        Toast.makeText(WritingAndTranslationExamActivity.this,"已收藏",Toast.LENGTH_SHORT).show();
                    }else {
                        isCollected = false;
                        ivIConCollection.setImageResource(R.drawable.icon_collection_befor);
                        if (mType.equals("translation")) {
                            if (collectedTranslation.contains(mTranslation)){
                                collectedTranslation.remove(mTranslation);
                            }
                            //              collectionBank.remove(mTranslation);
                            //             LogUtils.i("collectedTranslation",collectionBank.getCollectedTranslations().toString());
                        } else {
                            if (collectedWriting.contains(mWriting)){
                                collectedWriting.remove(mWriting);
                            }
                            //              collectionBank.remove(mWriting);
                            //               LogUtils.i("collectedWriting",collectionBank.getCollectedWritings().toString());
                        }
                        Toast.makeText(WritingAndTranslationExamActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    ToastUtils.show("登录后可使用收藏功能");
                }

            }
        });

        //
        tv_testpaper_name = findViewById(R.id.tv_test_paper_name);
        tv_testpaper_name.setText(year+"年"+month+"月 第"+index+"套");

        // 题目设置
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(title);

        //用于替换题目
        tvReplaceTitle = findViewById(R.id.tv_replace_title);
        tvReplaceTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvReplaceTitle.setVisibility(View.GONE);
                tv_title.setVisibility(View.VISIBLE);
            }
        });

        // 写作模块
        ed_content = findViewById(R.id.ed_content);
        ed_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               tv_title.setVisibility(View.GONE);
               tvReplaceTitle.setVisibility(View.VISIBLE);
            }
        });
        ed_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_title.setVisibility(View.GONE);
                tvReplaceTitle.setVisibility(View.VISIBLE);
                if (s.length() == 0){
                    tvReplaceTitle.setVisibility(View.GONE);
                    tv_title.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 处理作文，准备评分所需要的一些数据，交卷后传给ResultActivity
     * @param content 作文内容
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

    /**
     * 从map中取出收藏的写作和翻译题
     */
    private void initCollectedWritingAndTranslation(final String telephone){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String,Object> map = MySqlDBOpenHelper.getUserInfoFormMySql(telephone);

                if (map != null){
                    String s = "";
                    for (String key : map.keySet()){
                        s += key + ":" + map.get(key) + "\n";
                    }
                    LogUtils.i("查询结果",s);

                    Gson gson = new Gson();
                    if (map.get("collectedWriting") != null){
                        Type type = new TypeToken<ArrayList<Writing>>(){}.getType();
                        collectedWriting = gson.fromJson(map.get("collectedWriting").toString(),type);
                    }
                    if (map.get("collectedTranslation") != null){
                        Type type = new TypeToken<ArrayList<Translation>>(){}.getType();
                        collectedTranslation = gson.fromJson(map.get("collectedTranslation").toString(),type);
                    }
                }else {
                    ToastUtils.show(WritingAndTranslationExamActivity.this,"查询结果为空");
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (am.isOnline()){

            Gson gson = new Gson();
            final String inputCollectedWriting = gson.toJson(collectedWriting);
            final String inputCollectedTranslation = gson.toJson(collectedTranslation);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Connection conn = MySqlDBOpenHelper.getConn();
                    String sql_insert = "update user set collectedWriting=?,collectedTranslation=? " +
                            "where telephone = "+telephone;
                    if (conn != null){
                        try {

                            PreparedStatement pstm = conn.prepareStatement(sql_insert);
                            pstm.setString(1, inputCollectedWriting);
                            pstm.setString(2, inputCollectedTranslation);
                            pstm.executeUpdate();

                            if (pstm != null){
                                pstm.close();
                            }
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();

        }

    }
}