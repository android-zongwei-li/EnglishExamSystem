package com.example.activity.acticity_in_fragment1.words;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.activity.base.BaseAppCompatActivity;
import com.example.beans.Word;
import com.example.myapplication.R;
import com.example.utils.SqliteDBUtils;
import com.example.view.topbar.TopBar;

import java.util.ArrayList;

/**
 * 这个类负责：单词练习
 *
 */
public class WordTestActivity extends BaseAppCompatActivity {

    // 显示单词总数
    TextView tv_words_all_number;
    // 显示 已学单词
    ImageView iv_study_words;

    //存储所有单词
    private ArrayList<Word> wordsList = new ArrayList<>();
    // 准备 传给 StudyWordsActivity 的单词数据
    private ArrayList<Word> preparingWords;

    //单词本是否初始化了
    boolean isInitData = false;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            tv_words_all_number.setText(wordsList.size()+"");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_test);

        TopBar topBar = findViewById(R.id.topBar);
        topBar.setRighttIsVisable(false);
        topBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });

        // 单词本模块，点击后加载单词数据
        LinearLayout ll_words_book = findViewById(R.id.ll_words_book);
        ll_words_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isInitData){

                    //打开数据库输出流
                    SqliteDBUtils s = new SqliteDBUtils();
                    SQLiteDatabase db = s.openDatabase(getApplicationContext());
                    //查询数据库中cet4_words表中的所有数据
                    Cursor cursor = db.rawQuery("select * from cet4_words",null);
                    while(cursor.moveToNext()){
                        String word = cursor.getString(cursor.getColumnIndex("words"));
                        String phonetic = cursor.getString(cursor.getColumnIndex("phonetic"));
                        String chinese = cursor.getString(cursor.getColumnIndex("chinese"));
                        Word st = new Word(word,phonetic,chinese);
                        wordsList.add(st);
                    }
                    cursor.close();

                    handler.sendEmptyMessage(0);
                    isInitData = true;
                }else {
                    Toast.makeText(getBaseContext(),"当前单词本已加载", Toast.LENGTH_LONG).show();
                }

            }
        });

        tv_words_all_number = findViewById(R.id.tv_words_all_number);

        // 设置想要学习的单词数
        final EditText et_words_number = findViewById(R.id.et_words_number);
        et_words_number.setInputType(InputType.TYPE_CLASS_PHONE);

        // 开始学习按钮
        iv_study_words = findViewById(R.id.iv_study_words);
        iv_study_words.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 首先判断是否加载了单词本
                if (wordsList.size() == 0){
                    Toast.makeText(getBaseContext(),"请先选择词汇书", Toast.LENGTH_LONG).show();
                    return;
                }

                // 加载单词本后，设置每次要学习的单词数
                String number = et_words_number.getText().toString();
                if (!number.equals("")){

                    int num = Integer.parseInt(number);

                    if (num == 0){
                        Toast.makeText(getBaseContext(),"每次至少学习一个单词~", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (num > wordsList.size()){
                        Toast.makeText(getBaseContext(),
                                "当前单词本最多 "+wordsList.size()+" 个哦",
                                Toast.LENGTH_LONG).show();
                        et_words_number.setText("");
                        return;
                    }

                    initPreparingWords(num);
                } else {    // number为空，没有设置要学习的单词数，默认为9
                    initPreparingWords(9);
                }

                Intent intent = new Intent(WordTestActivity.this,StudyWordsActivity.class);
                intent.putExtra(StudyWordsActivity.WORDS,preparingWords);
                startActivity(intent);

                preparingWords.clear();
            }
        });

    }

    /**
     * 初始化要学习的单词集合
     * preparingWords
     * 长度为length
     */
    private void initPreparingWords(int length){
        preparingWords = new ArrayList<>(length);
        preparingWords.addAll(wordsList.subList(0,length));
    }

    public void onClickWordBook(View view){
        switch (view.getId()){
            case R.id.rb_learned_words:
                Intent intent = new Intent(this, LearnedWordsActivity.class);
                startActivity(intent);
                break;
            case R.id.rb_unfamiliar_words:
                Intent intent1 = new Intent(this, UnfamiliarWordsActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

}
