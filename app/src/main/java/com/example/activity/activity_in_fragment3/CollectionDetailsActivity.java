package com.example.activity.activity_in_fragment3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.CollectionRVAdapter;
import com.example.beans.CollectedListening;
import com.example.beans.CollectedReading;
import com.example.beans.CollectionBank;
import com.example.beans.Translation;
import com.example.beans.Writing;
import com.example.myapplication.R;
import com.example.utils.AccountManager;
import com.example.utils.LogUtils;
import com.example.utils.MySqlDBOpenHelper;
import com.example.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by: lzw.
 * Date: 2020/4/29
 * Time: 12:07
 * Description：
 * 1、
 */
public class CollectionDetailsActivity extends AppCompatActivity {
    public static final String QUESTION_TYPE = "question_type";//题型

    //已收藏写作
    private ArrayList<Writing> collectedWriting = new ArrayList<>();
    //已收藏听力题
    private ArrayList<CollectedListening> collectedListening = new ArrayList<>();
    //已收藏阅读
    private ArrayList<CollectedReading> collectedReading = new ArrayList<>();
    //已收藏翻译
    private ArrayList<Translation> collectedTranslation = new ArrayList<>();

    private CollectionRVAdapter adapter = null;
    private RecyclerView rv;

    private int question_type;

    private AccountManager am;
    private String telephone;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (question_type){
                case 0:
                    adapter = new CollectionRVAdapter(CollectionDetailsActivity.this,
                            collectedWriting,question_type);
                    break;
                case 1:
                    adapter = new CollectionRVAdapter(CollectionDetailsActivity.this,
                            collectedListening,question_type);
                    break;
                case 2:
                    adapter = new CollectionRVAdapter(CollectionDetailsActivity.this,
                            collectedReading,question_type);
                    break;
                case 3:
                    adapter = new CollectionRVAdapter(CollectionDetailsActivity.this,
                            collectedTranslation,question_type);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + question_type);
            }

            rv.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);

        am = AccountManager.getInstance(getApplication());
        //能够进入此页面，必然已经登录了，就不需在判断了
        telephone = am.getTelephone();
        initCollected(telephone);


        Intent intent = getIntent();
        question_type = intent.getIntExtra(QUESTION_TYPE,-1);

        rv = findViewById(R.id.rv_collection);
        rv.setLayoutManager(new
                LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

    }

    /**
     * 从map中取出收藏数据：collectedListening(收藏的听力题)
     */
    private void initCollected(final String telephone){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String,Object> map = MySqlDBOpenHelper.getUserInfoFormMySql(telephone);

                if (map != null){
                    String s = "";
                    for (String key : map.keySet()){
                        s += key + ":" + map.get(key) + "\n";
                    }
                    LogUtils.i("查询结果",s);

                    Gson gson = new Gson();
                    if (map.get("collectedWriting") != null){
                        Type type = new TypeToken<ArrayList<Writing>>(){}.getType();
                        collectedWriting = gson.fromJson(map.get("collectedWriting").toString(),type);
                    }
                    if (map.get("collectedTranslation") != null){
                        Type type = new TypeToken<ArrayList<Translation>>(){}.getType();
                        collectedTranslation = gson.fromJson(map.get("collectedTranslation").toString(),type);
                    }
                    if (map.get("collectedListening") != null){
                        Type type = new TypeToken<ArrayList<CollectedListening>>(){}.getType();
                        collectedListening = gson.fromJson(map.get("collectedListening").toString(),type);
                    }
                    if (map.get("collectedReading") != null){
                        Type type = new TypeToken<ArrayList<CollectedReading>>(){}.getType();
                        collectedReading = gson.fromJson(map.get("collectedReading").toString(),type);
                    }

                    mHandler.sendEmptyMessage(0);

                }else {
                    LogUtils.v("查询结果提示","查询结果为空");
                }
            }
        }).start();
    }
}
