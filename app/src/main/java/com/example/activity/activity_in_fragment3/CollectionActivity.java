package com.example.activity.activity_in_fragment3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        btnCollectionListening.setOnClickListener(this);
        btnCollectionWriting.setOnClickListener(this);
        Button btnCollectionReading = findViewById(R.id.btn_collection_reading);
        btnCollectionReading.setOnClickListener(this);
        btnCollectionTranslation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(CollectionActivity.this, CollectionDetailsActivity.class);
        switch (v.getId()){
            case R.id.btn_collection_writing:
                intent.putExtra(CollectionDetailsActivity.QUESTION_TYPE,0);
                break;
            case R.id.btn_collection_listening:
                intent.putExtra(CollectionDetailsActivity.QUESTION_TYPE,1);
                break;
            case R.id.btn_collection_reading:
                intent.putExtra(CollectionDetailsActivity.QUESTION_TYPE,2);
                break;
            case R.id.btn_collection_translation:
                intent.putExtra(CollectionDetailsActivity.QUESTION_TYPE,3);
                break;
            default:
                Toast.makeText(CollectionActivity.this,
                        "无",Toast.LENGTH_SHORT).show();
        }
        startActivity(intent);
    }
}
