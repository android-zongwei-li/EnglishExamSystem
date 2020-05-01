package com.example;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.activity.base.BaseAppCompatActivity;
import com.example.utils.testPaperUtils.TestPaperFromWord;
import com.example.utils.testPaperUtils.TestPaperFactory;
import com.example.fragment.Fragment1;
import com.example.fragment.Fragment2;
import com.example.fragment.Fragment3;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends BaseAppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private BottomNavigationView naview;
    private int pageCount = 3;

    // 退出对话框
    private AlertDialog dialog;
    private Button btn_determine,btn_cancel;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        initViews();
    }

    private void initData(){
        TestPaperFactory testPaperFactory = TestPaperFactory.getInstance();
        testPaperFactory.initData(MainActivity.this);
        List<TestPaperFromWord> testPaperList = testPaperFactory.getTestPaperList();
    }

    private void initViews() {
        mToolbar = findViewById(R.id.toobar_main_ac);
        mViewPager = findViewById(R.id.view_pager_main_ac);
        naview = findViewById(R.id.bov_main_nav);

        // toolBar 初始化
        getSupportActionBar();
        String title="英语考试系统";
        mToolbar.setTitle(title);
        for (int i = 0; i < mToolbar.getChildCount(); i++) {
            View view = mToolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (title.equals(textView.getText())) {
                    textView.setGravity(Gravity.CENTER);
                    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.CENTER;
                    textView.setLayoutParams(params);
                }
            }

        }

        //
        naview.setItemIconTintList(null);
        naview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
        mViewPager.setOffscreenPageLimit(pageCount);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                naview.getMenu().getItem(position).setChecked(true);
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
}
