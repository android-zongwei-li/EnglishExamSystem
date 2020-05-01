package com.example.activity.acticity_in_fragment1.writing;

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

import com.example.activity.base.BaseAppCompatActivity;
import com.example.beans.MySqliteManager;
import com.example.beans.Writing;
import com.example.myapplication.R;
import com.example.utils.SqliteDBUtils;
import com.example.view.topbar.TopBar;

import java.io.Serializable;
import java.util.ArrayList;

public class WritingActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        // 初始化 topbar
        TopBar topBar = findViewById(R.id.topBar);
        topBar.setTitle(R.string.title_writing);
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

        // listView 初始化
        final ArrayList<Writing> writingsList = new ArrayList<>();
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils();
        SQLiteDatabase db = sqliteDBUtils.openDatabase(getApplicationContext());
        Cursor cursor = db.rawQuery("select * from "+ MySqliteManager.TABLE_WRITING,null);
        while(cursor.moveToNext()){
            String questionID = cursor.getString(cursor.getColumnIndex(MySqliteManager.Writing.QUESTIONID));
            String year = cursor.getString(cursor.getColumnIndex(MySqliteManager.Writing.YEAR));
            String month = cursor.getString(cursor.getColumnIndex(MySqliteManager.Writing.MONTH));
            String index = cursor.getString(cursor.getColumnIndex(MySqliteManager.Writing.INDEX));
            String question = cursor.getString(cursor.getColumnIndex(MySqliteManager.Writing.QUESTION));
            String answer = cursor.getString(cursor.getColumnIndex(MySqliteManager.Writing.ANSWER));

            Writing writing = new Writing(questionID,year,month,index,question,answer);
            writingsList.add(writing);
        }
        cursor.close();
        ListView lv_writing = findViewById(R.id.lv_writing);
        lv_writing.setAdapter(new BaseAdapter() {
            /*
             * 为ListView设置一个适配器
             * getCount()返回数据个数
             * getView()为每一行设置一个条目
             * */
            @Override
            public int getCount() {
                return writingsList.size();
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
                    LayoutInflater inflater = WritingActivity.this.getLayoutInflater();
                    view = inflater.inflate(R.layout.writing_list_item,null);
                    //view = View.inflate(getBaseContext(),R.layout.item,null);
                }
                else
                {
                    view = convertView;
                }

                final Writing writing = writingsList.get(position);

                TextView tv_writing_year = view.findViewById(R.id.tv_writing_and_translation_year);
                TextView tv_writing_month  = view.findViewById(R.id.tv_writing_and_translation_month);
                TextView tv_writing_index  = view.findViewById(R.id.tv_writing_and_translation_index);

                tv_writing_year.setText(writing.getYear()+"年");
                tv_writing_month.setText(writing.getMonth()+"月");
                tv_writing_index.setText("第"+writing.getIndex()+"套");

                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //这段代码还可以优化一下
                        Intent intent = new Intent(WritingActivity.this, WritingExamActiviy.class);

                        intent.putExtra("writingBean", (Serializable) writing);

                        intent.putExtra(WritingExamActiviy.QUESTION, writing.getQuestion());
                        intent.putExtra(WritingExamActiviy.ANSWER, writing.getAnswer());
                        intent.putExtra(WritingExamActiviy.YEAR, writing.getYear());
                        intent.putExtra(WritingExamActiviy.MONTH, writing.getMonth());
                        intent.putExtra(WritingExamActiviy.INDEX, writing.getIndex());
                        intent.putExtra(WritingExamActiviy.QUESTIONID, writing.getQuestionID());

                        startActivity(intent);
                    }
                });

                return view;

            }

        });
    }

}
