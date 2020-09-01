package com.lzw.activity.activity_in_fragment1.reading;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.lzw.fragment.ReadingFragment;
import com.lzw.activity.base.BaseAppCompatActivity;
import com.lzw.englishExamSystem.R;
import com.lzw.view.topbar.TopBar;
import com.google.android.material.tabs.TabLayout;

public class ReadingActivity extends BaseAppCompatActivity {
    private final int[] titles = {R.string.reading_choose_word,
            R.string.reading_quickly,
            R.string.reading_carefully};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        //
        TopBar topBar = findViewById(R.id.topBar);
        topBar.setTitle(R.string.title_reading);
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
        for(int i = 0; i < titles.length; i++){
            tabLayout.addTab(tabLayout.newTab());
        }

        FmPagerAdapter pagerAdapter = new FmPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
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
            return ReadingFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }

}
