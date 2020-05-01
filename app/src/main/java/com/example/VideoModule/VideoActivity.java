package com.example.VideoModule;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.VideoModule.base.BasePager;
import com.example.VideoModule.pager.AudioPager;
import com.example.VideoModule.pager.NetAudioPager;
import com.example.VideoModule.pager.NetVideoPager;
import com.example.VideoModule.pager.VideoPager;
import com.example.myapplication.R;

import java.util.ArrayList;

public class VideoActivity extends FragmentActivity {

    private RadioGroup rg_bottom_tag;

    /**
     * 页面集合（本地视频，本地音频，网络视频，网络音频）
     */
    private ArrayList<BasePager> basePagers;

    // 选中位置
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        rg_bottom_tag = findViewById(R.id.rg_bottom_tag);

        basePagers = new ArrayList<>();
        basePagers.add(new VideoPager(this));
        basePagers.add(new AudioPager(this));
        basePagers.add(new NetVideoPager(this));
        basePagers.add(new NetAudioPager(this));

        // 设置RadioGroup的监听
        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        // 默认选中首页(本地视频)
        rg_bottom_tag.check(R.id.rb_video);

    }

    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId){
                default:
                    position = 0;
                    break;
                case R.id.rb_audio:
                    position = 1;
                    break;
                case R.id.rb_net_video:
                    position = 2;
                    break;
                case R.id.rb_net_audio:
                    position = 3;
                    break;
            }

            setFragment();

        }

    }

    /**
     *  把页面添加到Fragment中
     */
    private void setFragment() {
        //1.得到FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //3.替换
        fragmentTransaction.replace(R.id.fl_video_content,new ReplaceFragment(getBasePager()));
        //4.提交事务
        fragmentTransaction.commit();
    }

    public static class ReplaceFragment extends Fragment{

        private BasePager currentPager;

        public ReplaceFragment(BasePager pager){
            this.currentPager = pager;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            BasePager basePager = currentPager;
            if (basePager != null){
                // 各个页面的视图
                return basePager.rootView;
            }
            return null;
        }
    }

    /**
     * 根据位置得到对应的页面
     * @return
     */
    private BasePager getBasePager() {
        BasePager basePager = basePagers.get(position);
        if(basePager != null && ! basePager.isInitData){
            basePager.initData();

            basePager.isInitData = true;
        }
        return basePager;
    }

}
