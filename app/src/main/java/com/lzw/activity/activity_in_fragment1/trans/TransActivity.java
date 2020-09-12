package com.lzw.activity.activity_in_fragment1.trans;

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
import com.lzw.beans.Translation;
import com.lzw.englishExamSystem.R;
import com.lzw.utils.database.SqliteDBUtils;
import com.lzw.view.topbar.TopBar;

import java.util.ArrayList;

public class TransActivity extends BaseAppCompatActivity {

    private TextView tv_test_paper_name;    // 试卷名称

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        // 初始化 topbar
        TopBar topBar = findViewById(R.id.topBar);
        topBar.setTitle(R.string.title_translation);
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
        final ArrayList<Translation> translationsList = new ArrayList<>();
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils();
        SQLiteDatabase db = sqliteDBUtils.openDatabase(getApplicationContext());
        Cursor cursor = db.rawQuery("select * from "+ MySqliteManager.TABLE_TRANSLATION,
                null);
        while(cursor.moveToNext()){
            String questionID = cursor.getString(cursor.getColumnIndex(MySqliteManager.Translation.QUESTIONID));
            String year = cursor.getString(cursor.getColumnIndex(MySqliteManager.Translation.YEAR));
            String month = cursor.getString(cursor.getColumnIndex(MySqliteManager.Translation.MONTH));
            String index = cursor.getString(cursor.getColumnIndex(MySqliteManager.Translation.INDEX));
            String question = cursor.getString(cursor.getColumnIndex(MySqliteManager.Translation.QUESTION));
            String answer = cursor.getString(cursor.getColumnIndex(MySqliteManager.Translation.ANSWER));

            Translation translation = new Translation(questionID,year,month,index,question,answer);
            translationsList.add(translation);
        }
        cursor.close();

        ListView lv_trans = findViewById(R.id.lv_question);
        lv_trans.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return translationsList.size();
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
                View view ;
                if(convertView == null ) {
                    LayoutInflater inflater = TransActivity.this.getLayoutInflater();
                    view = inflater.inflate(R.layout.examination_paper_information_item,null); // 这个 item 布局 和 写作部分的可以使用同一个
                }
                else {
                    view = convertView;
                }

                final Translation translation = translationsList.get(position);

                tv_test_paper_name = view.findViewById(R.id.tv_item_test_paper_name);
                tv_test_paper_name.setText(translation.getYear()+"年"+translation.getMonth()+"月"+"-第"+translation.getIndex()+"套");

                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TransActivity.this, WritingAndTranslationExamActivity.class);

                        intent.putExtra(WritingAndTranslationExamActivity.TYPE,"translation");
                        intent.putExtra("transBean",translation);
                        intent.putExtra(WritingAndTranslationExamActivity.QUESTION, translation.getQuestion());
                        intent.putExtra(WritingAndTranslationExamActivity.ANSWER, translation.getAnswer());
                        intent.putExtra(WritingAndTranslationExamActivity.YEAR, translation.getYear());
                        intent.putExtra(WritingAndTranslationExamActivity.MONTH, translation.getMonth());
                        intent.putExtra(WritingAndTranslationExamActivity.INDEX, translation.getIndex());
                        intent.putExtra(WritingAndTranslationExamActivity.QUESTIONID, translation.getQuestionID());

                        startActivity(intent);
                    }
                });

                return view;

            }

        });
    }

}
