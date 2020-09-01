package com.lzw.activity.activity_in_fragment1.writing;

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

import com.lzw.activity.activity_in_fragment1.WritingAndTranslationExamActivity;
import com.lzw.activity.base.BaseAppCompatActivity;
import com.lzw.beans.MySqliteManager;
import com.lzw.beans.Writing;
import com.lzw.englishExamSystem.R;
import com.lzw.utils.SqliteDBUtils;
import com.lzw.view.topbar.TopBar;

import java.util.ArrayList;

public class WritingActivity extends BaseAppCompatActivity {

    private TextView tv_test_paper_name;    // 试卷名称

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

        ListView lv_writing = findViewById(R.id.lv_question);
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
                if(convertView == null ) {
                    LayoutInflater inflater = WritingActivity.this.getLayoutInflater();
                    view = inflater.inflate(R.layout.examination_paper_information_item,null);
                    //view = View.inflate(getBaseContext(),R.layout.item,null);
                }
                else {
                    view = convertView;
                }

                final Writing writingInfo = writingsList.get(position);

                tv_test_paper_name = view.findViewById(R.id.tv_item_test_paper_name);
                tv_test_paper_name.setText(writingInfo.getYear()+"年"+writingInfo.getMonth()+"月"+"-第"+writingInfo.getIndex()+"套");

                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //这段代码还可以优化一下
                        Intent intent = new Intent(WritingActivity.this,
                                WritingAndTranslationExamActivity.class);

                        intent.putExtra("writingBean", writingInfo);
                        intent.putExtra(WritingAndTranslationExamActivity.TYPE,"writing");
                        intent.putExtra(WritingAndTranslationExamActivity.QUESTION, writingInfo.getQuestion());
                        intent.putExtra(WritingAndTranslationExamActivity.ANSWER, writingInfo.getAnswer());
                        intent.putExtra(WritingAndTranslationExamActivity.YEAR, writingInfo.getYear());
                        intent.putExtra(WritingAndTranslationExamActivity.MONTH, writingInfo.getMonth());
                        intent.putExtra(WritingAndTranslationExamActivity.INDEX, writingInfo.getIndex());
                        intent.putExtra(WritingAndTranslationExamActivity.QUESTIONID, writingInfo.getQuestionID());

                        startActivity(intent);
                    }
                });

                return view;

            }

        });
    }

}
