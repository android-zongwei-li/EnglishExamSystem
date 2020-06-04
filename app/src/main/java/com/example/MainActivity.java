package com.example;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.activity.base.BaseAppCompatActivity;
import com.example.fragment.Fragment1;
import com.example.fragment.Fragment2;
import com.example.fragment.Fragment3;
import com.example.myapplication.R;
import com.example.utils.testPaperUtils.TestPaperFactory;
import com.example.view.topbar.TopBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

/**
 * 这个类用于构建程序的主界面。并负责初始化试题资源。
 *  {@link #initData()}
 *      初始化试题资源。
 *
 *  {@link #initViews()}
 *      主界面的视图由：
 *      {@link com.example.view.topbar.TopBar}
 *      {@link com.google.android.material.bottomnavigation.BottomNavigationView}+
 *      {@link androidx.viewpager.widget.ViewPager}+
 *      {@link androidx.fragment.app.Fragment} 构成。
 *
 */
public class MainActivity extends BaseAppCompatActivity {

    private TopBar topBar;
    private ViewPager mViewPager;
    private BottomNavigationView bnv;

    private int pageCount = 3;

    // 退出对话框
    private AlertDialog dialog;
    private Button btn_determine,btn_cancel;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // initLocaleLanguage();
        initData();

        initViews();
    }

    private void initData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                TestPaperFactory testPaperFactory = TestPaperFactory.getInstance();
                testPaperFactory.initData(MainActivity.this);
            }
        }).start();
   }

    private void initViews() {
        topBar = findViewById(R.id.topBar);
        topBar.setLeftIsVisable(false);
        topBar.setRighttIsVisable(false);
        topBar.setTitle(R.string.app_name);

        //
        bnv = findViewById(R.id.bov_main_nav);
        // 如果没有这句话，配置的颜色选择器将不会生效，icon的颜色不会随选择而变，全是黑色
        bnv.setItemIconTintList(null);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_exam:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.nav_basic:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.nav_mine:
                        mViewPager.setCurrentItem(2);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        //
        mViewPager = findViewById(R.id.view_pager_main_ac);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return getFragmentByPosition(position);
            }

            @Override
            public int getCount() {
                return pageCount;
            }
        });
        //参数为预加载数量，这个数量不能太多，保持最小值就可以了，之前使用的 pageCount 滑动时卡顿严重
        //这个改为1，效果并不明显，滑动依然卡顿
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bnv.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private Fragment getFragmentByPosition(int position) {
        if(position==0){
            return new Fragment1();
        }else if(position==1){
            return new Fragment2();
        }else if(position==2){
            return new Fragment3();
        }
        return null;
    }

    /**
     * 点击返回按钮，启动对话框动画
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断是否点击返回按钮
        if (keyCode == KeyEvent.KEYCODE_BACK){
            dialog = new AlertDialog.Builder(this).create();
            dialog.show();
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);  // 设置对话框在居中位置
            window.setWindowAnimations(R.style.ExitDialogStyle);    //设置对话框样式
            window.setContentView(R.layout.dialog_exit);    //设置对话框布局文件
            btn_determine = window.findViewById(R.id.btn_determine);
            btn_cancel = window.findViewById(R.id.btn_cancel);

            initEvent();
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 处理对话框中的按钮事件
     */
    private void initEvent() {

        btn_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    /**
     * 切换英语
     */
    private void initLocaleLanguage() {
        Resources resources = this.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = new Locale("en","as");
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());//更新配置
    }

}
