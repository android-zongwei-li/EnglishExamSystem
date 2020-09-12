package com.lzw.activity.activity_in_fragment1.words;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lzw.activity.base.BaseAppCompatActivity;
import com.lzw.beans.Word;
import com.lzw.englishExamSystem.R;
import com.lzw.service.AudioService;
import com.lzw.utils.AccountManager;
import com.lzw.utils.LogUtils;
import com.lzw.utils.database.MySqlDBOpenHelper;
import com.lzw.utils.ToastUtils;
import com.lzw.view.topbar.TopBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class StudyWordsActivity extends BaseAppCompatActivity {
    // 此页面需要学习的单词数据
    public static final String WORDS = "words";

    // 即将学习的单词
    private ArrayList<Word> words = new ArrayList<>();
    // 已学单词
    private ArrayList<Word> learnedWordsBook = new ArrayList<>();
    // 生词
    private ArrayList<Word> unfamiliarWordsBook = new ArrayList<>();

    // 标识 当前是 第几个单词
    private int indexWord = 0;

    //
    TopBar topBar;

    // 显示 当前第几题，共多少题，如： 1/10
    TextView tv_word_index;

    // 展示 当前学习的单词
    TextView tv_study_word_display;
    // 音标，点击后播放单词读音
    TextView tv_study_phonetic;
    // 展示 提示，点击不认识 后 ，显示 中文意思
    TextView tv_chinese;
    // 认识
    Button btn_understand;
    // 不认识
    Button btn_dont_understand;
    //
    Button btn_next;
    // 容纳 下一个 按钮，点击显示下一个单词
    LinearLayout ll_next_word;
    // 容纳 认识 和 不认识 两个按钮的，在特定情况下隐藏或显示
    LinearLayout ll_option;


    private AlertDialog alertDialog;
    private TextView tv_dialog_title,tv_dialog_content;// dialog中的 title 和 content
    private Button btn_dialog_determine,btn_dialog_cancel;//dialog中的按钮

    //账户信息
    private AccountManager am;
    private String telephone;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0 :    //认识单词,显示下一个,直到最后一个
                    if (indexWord == words.size()){ // 最后一个单词，点击认识后

                        ll_option.setVisibility(View.GONE);

                        // 弹出窗口提示：测试完成，显示成绩
                        new AlertDialog.Builder(StudyWordsActivity.this)
                                .setMessage("测试完成").show();
                    }
                    if (indexWord < words.size()){
                        tv_study_word_display.setText(words.get(indexWord).getWord());
                        tv_study_phonetic.setText(words.get(indexWord).getPhonetic());

                        tv_word_index.setText( (indexWord+1) +"/"+words.size());
                    }
                    break;
                case 1: // 不认识，显示中文意思,隐藏认识、不认识按钮，显示 下一个 按钮

                    if (indexWord < words.size()){
                        tv_chinese.setText(words.get(indexWord).getChinese());
                        ll_option.setVisibility(View.GONE);
                        ll_next_word.setVisibility(View.VISIBLE);

                        // 最后一个单词，点击不认识后,显示中文意思
                        if (indexWord == words.size()-1){
                            // 最后一个单词，不显示下一个，把btn_next的文字信息改掉
                            btn_next.setText("学习完成");
                            btn_next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    new AlertDialog.Builder(StudyWordsActivity.this)
                                            .setMessage("测试完成").show();

                                }
                            });
                        }

                    }
                    break;
                case 2: // 下一个，显示下一个单词，恢复提示语
                    if (indexWord == words.size()){ // 最后一个单词完成后，点击认识或不认识后 弹出窗口提示：测试完成，显示成绩
                        new AlertDialog.Builder(StudyWordsActivity.this)
                                .setMessage("测试完成").show();
                    }
                    if (indexWord < words.size()){
                        tv_word_index.setText( (indexWord+1) +"/"+words.size());
                        tv_study_word_display.setText(words.get(indexWord).getWord());
                        tv_study_phonetic.setText(words.get(indexWord).getPhonetic());
                        tv_chinese.setText("请说出单词发音和中文意思");
                        ll_next_word.setVisibility(View.GONE);
                        ll_option.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_words);

        am = AccountManager.getInstance(getApplication());
        telephone = am.getTelephone();
        if (am.isOnline()){
            initUnfamiliarAndLearnedBook(telephone);
        }else {
            ToastUtils.show("登录后可以同步学习记录");
        }

        final Intent intent = getIntent();
        words = (ArrayList<Word>) intent.getSerializableExtra(StudyWordsActivity.WORDS);

        topBar = findViewById(R.id.topBar);
        topBar.setRighttIsVisable(false);
        topBar.setTitle(R.string.title_study_words);
        topBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });

        tv_word_index = findViewById(R.id.tv_word_index);
        //
        tv_word_index.setText( (indexWord+1) +"/"+words.size());

        tv_study_word_display = findViewById(R.id.tv_study_word_display);
        //默认显示第一个,每显示完一个，把indexWord 加一
        tv_study_word_display.setText(words.get(indexWord).getWord());

        tv_study_phonetic = findViewById(R.id.tv_study_phonetic);
        tv_study_phonetic.setText(words.get(indexWord).getPhonetic());
        tv_study_phonetic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(StudyWordsActivity.this, AudioService.class);
                intent1.putExtra(AudioService.QUERY,tv_study_word_display.getText().toString());
                startService(intent1);
            }
        });

        tv_chinese = findViewById(R.id.tv_chinese);

        ll_option = findViewById(R.id.ll_option);

        btn_understand = findViewById(R.id.btn_understand);
        // 发送消息，显示下一个单词
        btn_understand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addWordToLearnedWordsBook(indexWord);

                indexWord++;
                handler.sendEmptyMessage(0);
            }
        });

        btn_dont_understand = findViewById(R.id.btn_dont_understand);
        // 发送消息，显示 中文意思
        btn_dont_understand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addWordToLearnedWordsBook(indexWord);
                addWordToUnfamiliarWordsBook(indexWord);

                handler.sendEmptyMessage(1);
            }
        });

        ll_next_word = findViewById(R.id.ll_next_word);

        ///   这有个 疑问：为什么给 ll_next_word 注册点击事件没用呢？
/*        ll_next_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessage(2);
            }
        });*/

        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexWord++;
                handler.sendEmptyMessage(2);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            window.setContentView(R.layout.dialog_exit);
            tv_dialog_title = window.findViewById(R.id.tv_dialog_title);
            tv_dialog_content = window.findViewById(R.id.tv_dialog_content);
            btn_dialog_determine = window.findViewById(R.id.btn_determine);
            btn_dialog_cancel = window.findViewById(R.id.btn_cancel);
            tv_dialog_title.setText("仅有 "+(words.size()-indexWord)+" 个就完成啦");
            tv_dialog_content.setText("再坚持一下，半途而废可不好~");
            btn_dialog_determine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    finish();
                }
            });
            btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击认识或者不认识 此单词后，要查询当前单词是否存在于已学单词本中
     * 如果已经有了，则不处理
     * 如果没有，就把这个单词加到已学单词本中
     */
    private void addWordToLearnedWordsBook(int indexWord){
        // 要处理的单词，添加还是不添加
        Word word = words.get(indexWord);
        // 这里新建了一个word对象，默认肯定是不包含的，根据contains的源码可知，
        // 需要重写 word 类 的 equals方法，使得当 这个单词的 英文意思一样时，就视为相等，即包含
        // 如果不包含，就把当前单词加进去
        if (!learnedWordsBook.contains(word)){
            learnedWordsBook.add(word);
        }
    }

    /**
     * 如果不认识当前单词，就判断生词本里有这个单词吗
     * 没有，添加
     * 有，do nothing
     * @param indexWord
     */
    private void addWordToUnfamiliarWordsBook(int indexWord){
        Word word = words.get(indexWord);

        if (!unfamiliarWordsBook.contains(word)){
            unfamiliarWordsBook.add(word);
        }
    }

    /**
     * 从map中取出数据：learnedWordsBook(已学单词本)、unfamiliarWordsBook(生词本)
     */
    private void initUnfamiliarAndLearnedBook(final String telephone){

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
                    Type type = new TypeToken<ArrayList<Word>>(){}.getType();
                    if (map.get("learnedWordsBook") != null){
                        learnedWordsBook = gson.fromJson(map.get("learnedWordsBook").toString(),type);
                    }
                    if (map.get("unfamiliarWordsBook") != null){
                        unfamiliarWordsBook = gson.fromJson(map.get("unfamiliarWordsBook").toString(),type);
                    }
                }else {
                    LogUtils.v("查询结果","查询结果为空");
                }
            }
        }).start();
    }

    /**
     * 当此activity回调此方法时，把当前的 生词、已学单词数据更新到数据库
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (am.isOnline()){

            Gson gson = new Gson();
            final String inputLearnedWordsBook = gson.toJson(learnedWordsBook);
            final String inoutUnfamiliarWordsBook = gson.toJson(unfamiliarWordsBook);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Connection conn = MySqlDBOpenHelper.getConn();
                    String sql_insert = "update user set learnedWordsBook=?,unfamiliarWordsBook=? " +
                            "where telephone = "+telephone;
                    if (conn != null){
                        try {

                            PreparedStatement pstm = conn.prepareStatement(sql_insert);
                            pstm.setString(1, inputLearnedWordsBook);
                            pstm.setString(2,inoutUnfamiliarWordsBook);
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
