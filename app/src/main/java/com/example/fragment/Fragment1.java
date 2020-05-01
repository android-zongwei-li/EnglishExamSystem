package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.activity.TranslateActivity;
import com.example.activity.acticity_in_fragment1.listening.ListeningActivity;
import com.example.activity.acticity_in_fragment1.reading.ReadingActivity;
import com.example.activity.acticity_in_fragment1.trans.TransActivity;
import com.example.activity.acticity_in_fragment1.WebViewActivity;
import com.example.activity.acticity_in_fragment1.words.WordTestActivity;
import com.example.activity.acticity_in_fragment1.writing.WritingActivity;
import com.example.myapplication.R;
import com.example.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment1 extends Fragment {

    Banner banner;
    List images = new ArrayList<>();
    List titles = new ArrayList<>();

    View view;

    // 显示当前日期
    private TextView tv_date;
    private TextView tv_dayDistance;

    // 四种题型模块
    private GridView gv_examItem;
    private List<Map<String, Object>> data_list = new ArrayList<>();;
    private SimpleAdapter sim_adapter;

    // 单词模块
    LinearLayout ll_words;

    // 真题实练模块
    LinearLayout ll_testpaper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_1,null);

        initBanner();

        initDayDistanceTV();

        //  以下为  考试题型 控件的设计
        gv_examItem = view.findViewById(R.id.gv_examItem);
        initGridView();

        // 真题模块
        ll_testpaper = view.findViewById(R.id.ll_testpaper);
        ll_testpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"真题",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), TranslateActivity.class));
            }
        });

        // 单词模块
        ll_words = view.findViewById(R.id.ll_words);
        ll_words.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), WordTestActivity.class));
            }
        });

        return view;
    }

    private void initBanner(){
        banner = view.findViewById(R.id.banner_in_fragment1);

        //添加图片资源
        images.add("http://img.lanrentuku.com/img/allimg/0906/8_220800_1.jpg");
        images.add("http://img.lanrentuku.com/img/allimg/1506/14332974643135.jpg");
        images.add("http://img.lanrentuku.com/img/allimg/1506/14343524956456.jpg");
        titles.add("图片——1");
        titles.add("图片——2");
        titles.add("图片——3");

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                String[] url = {"http://www.baidu.com","http://www.csdn.net",
                        "https://m.51test.net/cet/baoming/",
                        "http://cet-bm.neea.edu.cn/"};
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL,url[position]);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化 距离四级考试时间的 TextView
     */
    public void initDayDistanceTV(){
        // --------  以下为  距离四级考试天数 控件 的设计 --------
        tv_date = view.findViewById(R.id.tv_date);
        tv_dayDistance = view.findViewById(R.id.tv_dayDistance);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String sDate = dateFormat.format(new Date());
        tv_date.setText(sDate);

        //  日差计算还不准
        Date todayDate = new Date(Integer.parseInt(sDate.substring(0,4)),Integer.parseInt(sDate.substring(5,7)),Integer.parseInt(sDate.substring(8,10)));
        Date cet4_Date = new Date(2020,6,13);

        tv_dayDistance.setText(calcDayOffset(todayDate,cet4_Date)+"");

    }

    /**
     *
     * date2比date1多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int calcDayOffset(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {  //同一年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {  //闰年
                    timeDistance += 366;
                } else {  //不是闰年

                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else { //不同年
            return day2 - day1;
        }
    }

    /**
     * 初始化数据，构建GridView
     * @return
     */
    public void initGridView(){

        // 图片封装为一个数组
        int[] icon = { R.drawable.listening, R.drawable.reading,
                R.drawable.trans, R.drawable.write };
        String[] iconName = { "听力", "阅读", "翻译", "写作"};

        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        //新建适配器
        String [] from ={"image","text"};
        int [] to = {R.id.image, R.id.text};
        sim_adapter = new SimpleAdapter(getContext(), data_list, R.layout.item, from, to);
        //配置适配器
        gv_examItem.setAdapter(sim_adapter);

        gv_examItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    startActivity(new Intent(getContext(), ListeningActivity.class));
                }
                if (position == 1){
                    startActivity(new Intent(getContext(), ReadingActivity.class));
                }
                if (position == 2){
                    startActivity(new Intent(getContext(), TransActivity.class));
                }
                if (position == 3){
                    startActivity(new Intent(getContext(), WritingActivity.class));
                }
            }

        });

    }

}
