package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.activity.activity_in_fragment3.CollectionDetailsActivity;
import com.example.myapplication.R;

/**
 * Created by: lzw.
 * Date: 2020/4/29
 * Time: 11:06
 * Description：
 * 1、
 */
public class CollectionActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        Button btnCollectionWriting = findViewById(R.id.btn_collection_writing);
        btnCollectionWriting.setOnClickListener(this);
        Button btnCollectionTranslation = findViewById(R.id.btn_collection_translation);
        btnCollectionTranslation.setOnClickListener(this);
        Button btnCollectionListening = findViewById(R.id.btn_collection_listening);
        btnCollectionWriting.setOnClickListener(this);
        Button btnCollectionReading = findViewById(R.id.btn_collection_reading);
        btnCollectionTranslation.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(CollectionActivity.this, CollectionDetailsActivity.class);
        startActivity(intent);
    }
}
