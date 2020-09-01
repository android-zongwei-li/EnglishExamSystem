package com.lzw.activity.activity_in_fragment1.listening;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lzw.activity.base.BaseAppCompatActivity;
import com.lzw.englishExamSystem.R;
import com.lzw.utils.LogUtils;
import com.lzw.view.topbar.TopBar;

public class ListeningActivity extends BaseAppCompatActivity {

    private int[] titles = {R.string.listening_short_news,
            R.string.listening_conversation, R.string.listening_passages};

    private ViewPager viewPager;
    ListeningActivity.FmPagerAdapter pagerAdapter;
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
        final TopBar topBar = findViewById(R.id.topBar);
        topBar.setTitle(R.string.title_listening);
        topBar.setRightButtonText("筛选");
        topBar.setRighttIsVisable(true);
        topBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                //TODO:显示出筛选控件，通过筛选控件查询特定的试卷
                LinearLayout llFilterPanel = pagerAdapter.getCurrentFragment().getFilterPanel();
                if (llFilterPanel.getVisibility() == View.GONE){
                    llFilterPanel.setVisibility(View.VISIBLE);
                }else {
                    llFilterPanel.setVisibility(View.GONE);
                }
            }
        });

        //
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new ListeningActivity.FmPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        for (int i = 0; i < titles.length; i++) {
            tabLayout.addTab(tabLayout.newTab());
        }

        // 此方法会清除tab,导致tablayout标题不显示，所以标题需要在后面设置
        // 参考：https://blog.csdn.net/u013233097/article/details/54708202
        tabLayout.setupWithViewPager(viewPager, false);

        for (int i = 0; i < titles.length; i++) {
            tabLayout.getTabAt(i).setText(titles[i]);
        }
    }

    //ViewPager适配器
    private class FmPagerAdapter extends FragmentPagerAdapter {
        private ListeningFragment mCurrentFragment;

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

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mCurrentFragment = (ListeningFragment) object;
            super.setPrimaryItem(container, position, object);
        }

        /**
         * 通过此方法，就可以在Activity中获取ViewPager当前的Fragment了，需要重写  setPrimaryItem  方法
         * @return
         */
        public ListeningFragment getCurrentFragment() {
            return mCurrentFragment;
        }
    }

}
