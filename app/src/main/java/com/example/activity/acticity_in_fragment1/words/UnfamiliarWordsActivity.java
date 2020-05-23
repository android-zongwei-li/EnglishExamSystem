package com.example.activity.acticity_in_fragment1.words;

import android.os.Bundle;

import com.example.activity.base.BaseAppCompatActivity;
import com.example.myapplication.R;
import com.example.view.topbar.TopBar;

/**
 * Created by: lzw.
 * Date: 2020/5/23
 * Time: 22:43
 * Description：
 * 1、
 * 显示所有不熟悉的单词，即所有在 StudyWordsActivity 中选过 不认识 按钮的单词
 */
public class UnfamiliarWordsActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unfamiliar_words);

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
    }
}
