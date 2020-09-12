package com.lzw.activity.activity_in_fragment1.words;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lzw.activity.base.BaseAppCompatActivity;
import com.lzw.adapter.WordsListRVAdapter;
import com.lzw.beans.Word;
import com.lzw.englishExamSystem.R;
import com.lzw.utils.AccountManager;
import com.lzw.utils.LogUtils;
import com.lzw.utils.database.MySqlDBOpenHelper;
import com.lzw.view.topbar.TopBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by: lzw.
 * Date: 2020/5/23
 * Time: 22:39
 * Description：
 * 1、
 * 已经学过的单词，即所有在 StudyWordsActivity 中选过 认识 或 不认识 按钮的单词
 */
public class LearnedWordsActivity extends BaseAppCompatActivity {

    // 标记：
    // 这个类和 UnfamiliarWordsActivity 是高度相似的，后面有空的话，
    // 思考下，有无必要把两个何在一起
    // 并且这两个类中包括涉及到的相关类，应该还可以再优化下代码

    ArrayList<Word> learnedWordsBook = new ArrayList<>();

    RecyclerView rv;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            WordsListRVAdapter adapter = new WordsListRVAdapter(learnedWordsBook);
            rv.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unfamiliar_words);

        AccountManager am = AccountManager.getInstance(getApplication());
        String telephone = am.getTelephone();
        getLearnedWordsBook(telephone);

        TopBar topBar = findViewById(R.id.topBar);
        topBar.setTitle(R.string.title_learned_words_book);
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

        rv = findViewById(R.id.rv_unfamiliar_words);
        rv.setLayoutManager(new
                LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

    }

    /**
     * 从map中取出数据：learnedWordsBook(已学单词本)
     */
    private void getLearnedWordsBook(final String telephone){

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
                        mHandler.sendEmptyMessage(0);
                        LogUtils.i("learnedWordsBook",learnedWordsBook.toString());
                    }
                }else {
                    LogUtils.i("查询结果","map为空");
                }
            }
        }).start();
    }
}
