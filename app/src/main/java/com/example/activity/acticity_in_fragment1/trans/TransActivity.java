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

import com.example.activity.acticity_in_fragment1.WritingAndTranslationExamActivity;
import com.example.activity.base.BaseAppCompatActivity;
import com.example.beans.MySqliteManager;
import com.example.beans.Translation;
import com.example.myapplication.R;
import com.example.utils.SqliteDBUtils;
import com.example.view.topbar.TopBar;

import java.util.ArrayList;

public class TransActivity extends BaseAppCompatActivity {

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
        ListView lv_trans = findViewById(R.id.lv_writing);
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
                    view = inflater.inflate(R.layout.writing_list_item,null); // 这个 item 布局 和 写作部分的可以使用同一个
                }
                else {
                    view = convertView;
                }

                final Translation translation = translationsList.get(position);

                TextView tv_year = view.findViewById(R.id.tv_writing_and_translation_year);
                TextView tv_month  = view.findViewById(R.id.tv_writing_and_translation_month);
                TextView tv_index  = view.findViewById(R.id.tv_writing_and_translation_index);
                tv_year.setText(translation.getYear()+"年");
                tv_month.setText(translation.getMonth()+"月");
                tv_index.setText("第"+translation.getIndex()+"套");

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
