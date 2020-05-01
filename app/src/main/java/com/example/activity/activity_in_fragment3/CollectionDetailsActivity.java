package com.example.activity.activity_in_fragment3;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adapter.CollectionRVAdapter;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);

        CollectionBank collectionBank = CollectionBank.getInstance();

        RecyclerView rv = findViewById(R.id.rv_collection);
        rv.setLayoutManager(new
                LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        CollectionRVAdapter adapter = new CollectionRVAdapter(this,collectionBank.getCollectedWritings());
        rv.setAdapter(adapter);
    }
}
