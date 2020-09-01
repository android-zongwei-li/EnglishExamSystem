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
import com.lzw.utils.MySqlDBOpenHelper;
import com.lzw.view.topbar.TopBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by: lzw.
 * Date: 2020/5/23
 * Time: 22:43
 * Description：
 * 1、
 * 显示所有不熟悉的单词，即所有在 StudyWordsActivity 中选过 不认识 按钮的单词
 */
public class UnfamiliarWordsActivity extends BaseAppCompatActivity {

    private ArrayList<Word> unfamiliarWordsBook = new ArrayList<>();

    RecyclerView rv;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            WordsListRVAdapter adapter = new WordsListRVAdapter(unfamiliarWordsBook);
            rv.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unfamiliar_words);

        AccountManager am = AccountManager.getInstance(getApplication());
        String telephone = am.getTelephone();

        getUnfamiliarWordsBook(telephone);

        TopBar topBar = findViewById(R.id.topBar);
        topBar.setTitle(R.string.title_unfamiliar_word_book);
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

        // 这边还可优化：在加载过程中，可以加入一个进度条，尽管基本上也是瞬间加载好，但
        // 考虑到数据庞大时，加载较慢，还是需要优化一下


        // 要注意数据是在新的线程中加载的，所以下面两行代码不能放在这里，
        // 不然 unfamiliarWordsBook集合，可能为空，测试了很多次都为空
        /*WordsListRVAdapter adapter = new WordsListRVAdapter(unfamiliarWordsBook);
        rv.setAdapter(adapter);*/
    }

    /**
     * 从map中取出数据：unfamiliarWordsBook(生词本)
     */
    private void getUnfamiliarWordsBook(final String telephone){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String,Object> map = MySqlDBOpenHelper.getUserInfoFormMySql(telephone);

                if (map != null){
                    String s = "";
                    for (String key : map.keySet()){
                        s += key + ":" + map.get(key) + "\n";
                    }
                    LogUtils.v("查询结果",s);

                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<Word>>(){}.getType();
                    if (map.get("unfamiliarWordsBook") != null){
                        unfamiliarWordsBook = gson.fromJson(map.get("unfamiliarWordsBook").toString(),type);
                        mHandler.sendEmptyMessage(0);
                        LogUtils.v("unfamiliarWordsBook",unfamiliarWordsBook.toString());
                    }
                }else {
                    LogUtils.v("查询结果","map为空");
                }
            }
        }).start();
    }
}
