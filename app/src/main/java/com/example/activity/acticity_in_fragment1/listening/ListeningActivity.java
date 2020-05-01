package com.example.activity.acticity_in_fragment1.listening;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.activity.base.BaseAppCompatActivity;
import com.example.myapplication.R;
import com.example.view.topbar.TopBar;
import com.google.android.material.tabs.TabLayout;


public class ListeningActivity extends BaseAppCompatActivity {
    private static final String TAG = "ListeningActivity";
    private static final String TITLE = "听力";

    private String[] titles = {"短篇新闻","情景对话","听力文章"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);

        //
        TopBar topBar = findViewById(R.id.topBar);
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
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.viewpager);
        ListeningActivity.FmPagerAdapter pagerAdapter = new ListeningActivity.FmPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        for(int i = 0; i < titles.length; i++){
            tabLayout.addTab(tabLayout.newTab());
        }

        // 此方法会清除tab,导致tablayout标题不显示，所以标题需要在后面设置
        // 参考：https://blog.csdn.net/u013233097/article/details/54708202
        tabLayout.setupWithViewPager(viewPager,false);

        for(int i = 0; i < titles.length; i++){
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
            return ListeningFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }

}
