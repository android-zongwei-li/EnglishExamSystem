package com.example.activity.activity_in_fragment1.listening;

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

    private int[] titles = {R.string.listening_short_news,
            R.string.listening_conversation,R.string.listening_passages};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);

        //连接服务器
        // 现在换了一种获取方式
        // 把音频放到tomcat的webapps/ROOT文件夹下，通过网页获取。
        // 这个暂时用不到了
 //       ConnectServer.getConn();

        //
        TopBar topBar = findViewById(R.id.topBar);
        topBar.setTitle(R.string.title_listening);
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
        ListeningActivity.FmPagerAdapter pagerAdapter =
                new ListeningActivity.FmPagerAdapter(getSupportFragmentManager());
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
