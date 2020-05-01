package com.example.activity.acticity_in_fragment1.reading;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.activity.acticity_in_fragment1.BlankFragment;
import com.example.activity.base.BaseAppCompatActivity;
import com.example.myapplication.R;
import com.example.view.topbar.TopBar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ReadingActivity extends BaseAppCompatActivity {

    private static final String TITLE = "阅读";
    private static final String TAG = "ReadingActivity";

    TopBar topBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FmPagerAdapter pagerAdapter;
    private String[] titles = {"选词填空","快速阅读","仔细阅读"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        initView();

        /*ll_choose_word = findViewById(R.id.ll_choose_word);
        ll_choose_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ReadingActivity.this,ChooseWordActivity.class);
                startActivity(intent);

            }
        });*/

    }

    private void initView() {

        //
        topBar = findViewById(R.id.topBar);
        topBar.setTitle(TITLE);
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

        //
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewpager);

        for(int i = 0; i < titles.length; i++){
            //         fragments.add(new TabFragment());
            tabLayout.addTab(tabLayout.newTab());
        }

        pagerAdapter = new FmPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        // 此方法会清除tab,导致tablayout标题不显示，所以标题需要在后面设置
        // 参考：https://blog.csdn.net/u013233097/article/details/54708202
        tabLayout.setupWithViewPager(viewPager,false);

        for(int i = 0; i < titles.length; i++){
            //         fragments.add(new TabFragment());
            tabLayout.getTabAt(i).setText(titles[i]);
        }

    }

    //ViewPager适配器
    private class FmPagerAdapter extends FragmentPagerAdapter {
        public FmPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return BlankFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }

}
