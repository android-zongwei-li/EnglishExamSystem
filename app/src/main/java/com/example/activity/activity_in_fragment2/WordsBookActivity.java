package com.example.activity.activity_in_fragment2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.MyApplication;
import com.example.activity.base.BaseAppCompatActivity;
import com.example.beans.Word;
import com.example.myapplication.R;
import com.example.utils.AccountManager;
import com.example.utils.LogUtils;
import com.example.utils.MySqlDBOpenHelper;
import com.example.utils.SqliteDBUtils;
import com.example.utils.ToastUtils;
import com.example.view.topbar.TopBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class WordsBookActivity extends BaseAppCompatActivity {
    public static final String TYPE = "TYPE";
    public static final int CEEE_BOOK = 0;
    public static final int CET4_BOOK = 1;

    private TopBar topBar;

    private ListView lv_words;
    private ArrayList<Word> wordslist;

    //存储搜索到的单词
    private ArrayList<Word> searchWords = new ArrayList<>();

    private ArrayList<Word> unfamiliarWordsBook = new ArrayList<>();

    AccountManager am = AccountManager.getInstance(MyApplication.getInstance());
    String telephone;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            //更新单词列表
            initListView(searchWords);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_book);

     //   initUnfamiliarBook();

        Intent intent = getIntent();
        int type = intent.getIntExtra(TYPE,3);

        topBar = findViewById(R.id.topBar);
        topBar.setTitle(getString(R.string.cet4_book));
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

        //
        SearchView searchWordView = findViewById(R.id.search_view_word);
        searchWordView.setIconifiedByDefault(false);
        searchWordView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ToastUtils.show(WordsBookActivity.this,"提交了");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchWords.clear();

                //遍历wordslist，查询要展示的数据
                String wordStr;
                for (Word word : wordslist){
                    wordStr = word.getWord();
                    if (wordStr.contains(newText.trim())){
                        LogUtils.v("测试",newText.trim());
                        searchWords.add(word);
                    }
                }
                mHandler.sendEmptyMessage(0);

                return false;
            }
        });

        // 加载数据
        if (type == CEEE_BOOK){
            initCeeeWords();
        }else {
            initCet4Words();
        }

        //
        lv_words = findViewById(R.id.lv_words);
        initListView(wordslist);
    }

    private void initListView(final ArrayList<Word> wordsList){
        lv_words.setAdapter(new BaseAdapter() {
            /*
             * 为ListView设置一个适配器
             * getCount()返回数据个数
             * getView()为每一行设置一个条目
             * */
            @Override
            public int getCount() {
                return wordsList.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = WordsBookActivity.this.getLayoutInflater();
                convertView = inflater.inflate(R.layout.word_list_item,null);


                final Word word = wordsList.get(position);
                //
                TextView tv_word = convertView.findViewById(R.id.tv_word);
                tv_word.setText(word.getWord());
                //
                final TextView tv_chinese  = convertView.findViewById(R.id.tv_chinese);
                tv_chinese.setText(word.getChinese());
                tv_chinese.setOnClickListener(new View.OnClickListener() {
                    boolean setBg = false;//tv_chinese是否设置了背景颜色
                    @Override
                    public void onClick(View v) {
                        if (!setBg){
                            tv_chinese.setBackgroundResource(R.color.textview_word_chinese_bg);
                            setBg = true;
                        }else {
                            tv_chinese.setBackground(null);
                            setBg = false;
                        }
                    }
                });
                /*//把单词添加到生词本
                final TextView tvAddWord = convertView.findViewById(R.id.iv_add_word);
                tvAddWord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (am.isOnline()){
                            //添加生词
                            if (!unfamiliarWordsBook.contains(word)){
                                unfamiliarWordsBook.add(word);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvAddWord.setText("生词");
                                        tvAddWord.setTextSize(20);
                                        tvAddWord.setBackground(getResources().getDrawable(R.drawable.border_text));
                                    }
                                });
                            }
                            *//*else {
                                unfamiliarWordsBook.remove(word);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvAddWord.setText("+");
                                        tvAddWord.setBackground(null);
                                    }
                                });
                            }*//*

                        }else {
                            ToastUtils.show(WordsBookActivity.this,getString(R.string.login_unfamiliar_book_tips));
                        }
                    }
                });*/

                return convertView;

            }

        });
    }

    /**
     * 初始化四级词汇
     */
    private void initCet4Words(){
        wordslist = new ArrayList<>();
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils();
        SQLiteDatabase db = sqliteDBUtils.openDatabase(getApplicationContext());
        if (db == null){
            return;
        }
        Cursor cursor = db.rawQuery("select * from cet4_words",null);
        while(cursor.moveToNext()){
            String word = cursor.getString(cursor.getColumnIndex("words"));
            String phonetic = cursor.getString(cursor.getColumnIndex("phonetic"));
            String chinese = cursor.getString(cursor.getColumnIndex("chinese"));
            Word st = new Word(word,phonetic,chinese);
            wordslist.add(st);
        }
    }

    /**
     * 初始化高考词汇
     */
    private void initCeeeWords(){
        wordslist = new ArrayList<>();
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils();
        SQLiteDatabase db = sqliteDBUtils.openDatabase(getApplicationContext());
        Cursor cursor = db.rawQuery("select * from ceee_3500_words",null);
        while(cursor.moveToNext()){
            String word = cursor.getString(cursor.getColumnIndex("words"));
            if (word.contains("？")||word.contains("?")){
                //高考词汇书中的单词，最后一个字符有的为？，需要去除
                word = word.substring(0,word.length()-1);
            }
            String phonetic = cursor.getString(cursor.getColumnIndex("phonetic"));
            String chinese = cursor.getString(cursor.getColumnIndex("chinese"));
            Word st = new Word(word,phonetic,chinese);
            wordslist.add(st);
        }
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
                        LogUtils.v("unfamiliarWordsBook",unfamiliarWordsBook.toString());
                    }
                }else {
                    LogUtils.v("查询结果","map为空");
                }
            }
        }).start();
    }

    /**
     * 初始化生词本
     */
    private void initUnfamiliarBook(){
        telephone = am.getTelephone();
        if (am.isOnline()){
            getUnfamiliarWordsBook(telephone);
        }else {
            //do nothing
        }
    }

    /*@Override
    protected void onPause() {
        super.onPause();

        //更新生词本
        if (am.isOnline()){

            Gson gson = new Gson();
            final String inoutUnfamiliarWordsBook = gson.toJson(unfamiliarWordsBook);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Connection conn = MySqlDBOpenHelper.getConn();
                    String sql_insert = "update user set unfamiliarWordsBook=? " +
                            "where telephone = "+telephone;
                    try {

                        PreparedStatement pstm = conn.prepareStatement(sql_insert);
                        pstm.setString(1,inoutUnfamiliarWordsBook);
                        pstm.executeUpdate();

                        if (pstm != null){
                            pstm.close();
                        }
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }*/
}
