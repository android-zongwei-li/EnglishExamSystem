package com.example.activity.activity_in_fragment3;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.CollectionRVAdapter;
import com.example.beans.CollectionBank;
import com.example.myapplication.R;

/**
 * Created by: lzw.
 * Date: 2020/4/29
 * Time: 12:07
 * Description：
 * 1、
 */
public class CollectionDetailsActivity extends AppCompatActivity {
    public static final String QUESTION_TYPE = "question_type";//题型

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);

        Intent intent = getIntent();
        int question_type = intent.getIntExtra(QUESTION_TYPE,-1);
        CollectionBank collectionBank = CollectionBank.getInstance();

        RecyclerView rv = findViewById(R.id.rv_collection);
        rv.setLayoutManager(new
                LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        CollectionRVAdapter adapter = null;
        switch (question_type){
            case 0:
                adapter = new CollectionRVAdapter(this,
                        collectionBank.getCollectedWritings(),question_type);
                break;
            case 1:
                adapter = new CollectionRVAdapter(this,
                        collectionBank.getCollectedListenings(),question_type);
                break;
            case 2:
                adapter = new CollectionRVAdapter(this,
                        collectionBank.getCollectedReading(),question_type);
                break;
            case 3:
                adapter = new CollectionRVAdapter(this,
                        collectionBank.getCollectedTranslations(),question_type);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + question_type);
        }
        rv.setAdapter(adapter);
    }
}
