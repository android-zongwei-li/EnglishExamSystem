package com.example.activity.acticity_in_fragment1.words;

import android.os.Bundle;

import com.example.activity.base.BaseAppCompatActivity;
import com.example.myapplication.R;
import com.example.view.topbar.TopBar;

/**
 * Created by: lzw.
 * Date: 2020/5/23
 * Time: 22:39
 * Description：
 * 1、
 * 已经学过的单词，即所有在 StudyWordsActivity 中选过 认识 或 不认识 按钮的单词
 */
public class LearnedWordsActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learned_words);

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
    }
}
