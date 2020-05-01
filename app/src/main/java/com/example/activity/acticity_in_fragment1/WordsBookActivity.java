package com.example.activity.acticity_in_fragment1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.activity.base.BaseAppCompatActivity;
import com.example.beans.Word;
import com.example.myapplication.R;
import com.example.utils.SqliteDBUtils;
import com.example.view.topbar.TopBar;

import java.util.ArrayList;

public class WordsBookActivity extends BaseAppCompatActivity {

    TopBar topBar;

    private ListView lv_words;
    private ArrayList<Word> wordslist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_book);

        topBar = findViewById(R.id.topBar);
        topBar.setTitle("CET-4 单词");
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

        wordslist = new ArrayList<>();
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils();
        SQLiteDatabase db = sqliteDBUtils.openDatabase(getApplicationContext());
        Cursor cursor = db.rawQuery("select * from cet4_words",null);
        while(cursor.moveToNext()){
            String word = cursor.getString(cursor.getColumnIndex("words"));
            String phonetic = cursor.getString(cursor.getColumnIndex("phonetic"));
            String chinese = cursor.getString(cursor.getColumnIndex("chinese"));
            Word st = new Word(word,phonetic,chinese);
            wordslist.add(st);
        }
        lv_words = findViewById(R.id.lv_words);
        lv_words.setAdapter(new BaseAdapter() {
            /*
             * 为ListView设置一个适配器
             * getCount()返回数据个数
             * getView()为每一行设置一个条目
             * */
            @Override
            public int getCount() {
                return wordslist.size();
            }

            @Override
            public Object getItem(int position) {
                // return studentlist.get(position);
                return null;
            }

            @Override
            public long getItemId(int position) {
                // return position;
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view;
                /**对ListView的优化，convertView为空时，创建一个新视图；
                 * convertView不为空时，代表它是滚出,
                 * 放入Recycler中的视图,若需要用到其他layout，
                 * 则用inflate(),同一视图，用findViewBy()
                 * **/
                if(convertView == null )
                {
                    LayoutInflater inflater = WordsBookActivity.this.getLayoutInflater();
                    view = inflater.inflate(R.layout.word_list_item,null);
                    //view = View.inflate(getBaseContext(),R.layout.item,null);
                }
                else
                {
                    view = convertView;
                }

                Word word = wordslist.get(position);
                TextView tv_word = view.findViewById(R.id.tv_word);
                final TextView tv_chinese  = view.findViewById(R.id.tv_chinese);

                tv_word.setText(word.getWord());
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

                return view;

            }

        });

    }

}
