package com.example.activity.acticity_in_fragment1.trans;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.activity.acticity_in_fragment1.writing.WritingExamActiviy;
import com.example.activity.base.BaseAppCompatActivity;
import com.example.beans.MySqliteManager;
import com.example.beans.Translation;
import com.example.myapplication.R;
import com.example.utils.SqliteDBUtils;
import com.example.view.topbar.TopBar;

import java.util.ArrayList;

public class TransActivity extends BaseAppCompatActivity {

    TopBar topBar;

    ListView lv_trans;
    private ArrayList<Translation> translationslist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView(){

        // 初始化 topbar
        topBar = findViewById(R.id.topBar);
        topBar.setTitle("翻译");
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

        /*// 初始化 ll_short_news：设置跳转
        ll_apply_info = findViewById(R.id.ll_apply_info);
        ll_apply_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(WritingActivity.this,WritingExamActiviy.class));
            }
        });*/

        // listView 初始化
        translationslist = new ArrayList<>();
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils();
        SQLiteDatabase db = sqliteDBUtils.openDatabase(getApplicationContext());
        Cursor cursor = db.rawQuery("select * from "+ MySqliteManager.TABLE_TRANSLATION,null);
        while(cursor.moveToNext()){
            String questionID = cursor.getString(cursor.getColumnIndex(MySqliteManager.Translation.QUESTIONID));
            String year = cursor.getString(cursor.getColumnIndex(MySqliteManager.Translation.YEAR));
            String month = cursor.getString(cursor.getColumnIndex(MySqliteManager.Translation.MONTH));
            String index = cursor.getString(cursor.getColumnIndex(MySqliteManager.Translation.INDEX));
            String question = cursor.getString(cursor.getColumnIndex(MySqliteManager.Translation.QUESTION));
            String answer = cursor.getString(cursor.getColumnIndex(MySqliteManager.Translation.ANSWER));

            Translation translation = new Translation(questionID,year,month,index,question,answer);
            translationslist.add(translation);
        }
        lv_trans = findViewById(R.id.lv_writing);
        lv_trans.setAdapter(new BaseAdapter() {
            /*
             * 为ListView设置一个适配器
             * getCount()返回数据个数
             * getView()为每一行设置一个条目
             * */
            @Override
            public int getCount() {
                return translationslist.size();
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

                View view ;
                /**对ListView的优化，convertView为空时，创建一个新视图；
                 * convertView不为空时，代表它是滚出,
                 * 放入Recycler中的视图,若需要用到其他layout，
                 * 则用inflate(),同一视图，用findViewBy()
                 * **/
                if(convertView == null )
                {
                    LayoutInflater inflater = TransActivity.this.getLayoutInflater();
                    view = inflater.inflate(R.layout.writing_list_item,null); // 这个 item 布局 和 写作部分的可以使用同一个
                    //view = View.inflate(getBaseContext(),R.layout.item,null);
                }
                else
                {
                    view = convertView;
                }

                final Translation translation = translationslist.get(position);

                TextView tv_year = view.findViewById(R.id.tv_writing_and_translation_year);
                TextView tv_month  = view.findViewById(R.id.tv_writing_and_translation_month);
                TextView tv_index  = view.findViewById(R.id.tv_writing_and_translation_index);

                tv_year.setText(translation.getYear()+"年");
                tv_month.setText(translation.getMonth()+"月");
                tv_index.setText("第"+translation.getIndex()+"套");

                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TransActivity.this, WritingExamActiviy.class);

                        intent.putExtra(WritingExamActiviy.QUESTION, translation.getQuestion());
                        intent.putExtra(WritingExamActiviy.ANSWER, translation.getAnswer());
                        intent.putExtra(WritingExamActiviy.YEAR, translation.getYear());
                        intent.putExtra(WritingExamActiviy.MONTH, translation.getMonth());
                        intent.putExtra(WritingExamActiviy.INDEX, translation.getIndex());
                        intent.putExtra(WritingExamActiviy.QUESTIONID, translation.getQuestionID());

                        startActivity(intent);
                    }
                });

                return view;

            }

        });

    }


}
