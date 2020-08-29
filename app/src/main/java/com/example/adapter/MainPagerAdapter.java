package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activity.TranslateActivity;
import com.example.activity.activity_in_fragment1.WebViewActivity;
import com.example.activity.activity_in_fragment1.listening.ListeningActivity;
import com.example.activity.activity_in_fragment1.reading.ReadingActivity;
import com.example.activity.activity_in_fragment1.trans.TransActivity;
import com.example.activity.activity_in_fragment1.words.WordTestActivity;
import com.example.activity.activity_in_fragment1.writing.WritingActivity;
import com.example.myapplication.R;
import com.example.utils.DateUtils;
import com.example.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页面
 * @author Zongwei Li
 */
public class MainPagerAdapter extends RecyclerView.Adapter {
    private Context mActivity;

    /**
     * 轮播图和考试时间倒计时
     */
    public static final int TYPE_BANNER = 0;
    /**
     * 水平测试模块
     */
    public static final int TYPE_LEVEL_TEST = 1;
    /**
     * 试卷各种题型模块，包括单词、听力、阅读、翻译、写作、真题实战
     */
    public static final int TYPE_PAPER = 2;
    /**
     * 其他模块，可以放一些视频之类的。
     */
    public static final int TYPE_OTHER = 3;

    /**
     * 什么都不显示
     */
    public static final int TYPE_NOTHING = 999;

    private List<Integer> itemControllerList = new ArrayList<>();

    public MainPagerAdapter(Context context) {
        mActivity = context;

        itemControllerList.add(0);
        itemControllerList.add(1);
        itemControllerList.add(2);
        itemControllerList.add(3);
    }

    @Override
    public int getItemViewType(int position) {
        int whichItem = itemControllerList.get(position);
        return whichItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case TYPE_BANNER:
                view = View.inflate(parent.getContext(), R.layout.layout_banner_and_tips,null);
                return new BannerHolder(view);
            case TYPE_PAPER:
                view = View.inflate(parent.getContext(), R.layout.layout_types_of_paper_question,null);
                return new PaperHolder(view);
            case TYPE_OTHER:
                view = View.inflate(parent.getContext(), R.layout.layout_other,null);
                return new OtherHolder(view);
            case TYPE_LEVEL_TEST:
                view = View.inflate(parent.getContext(), R.layout.layout_level_test,null);
                return new LevelTestHolder(view);
            default:
                view = new View(mActivity);
                //NothingHolder：参数不能为null
                return new NothingHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return itemControllerList.size();
    }


    /**
     * 轮播图，距离考试的时间
     * 提供并初始化 布局：layout_banner_and_tips
     */
    private class BannerHolder extends RecyclerView.ViewHolder {
        private Banner banner;

        private TextView tvDayDistance;
        // 显示当前日期
        private TextView tvDate;
        private List images = new ArrayList<>();
        private List titles = new ArrayList<>();

        public BannerHolder(@NonNull View itemView) {
            super(itemView);
            initBanner();
            initDayDistanceTV();
        }

        private void initBanner(){
            banner = itemView.findViewById(R.id.banner);

            //添加图片资源
            images.add(R.drawable.cet4_home);
            images.add(R.drawable.today_listening);
            images.add(R.drawable.ziyuanwang);
            titles.add("四级官网");
            titles.add("每日英语听力");
            titles.add("四级资讯网");

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
                    String[] url = {
                            "http://cet-bm.neea.edu.cn/",
                            "http://dict.eudic.net/ting/",
                            "https://m.51test.net/cet/"
                    };
                    Intent intent = new Intent(mActivity, WebViewActivity.class);
                    intent.putExtra(WebViewActivity.URL,url[position]);
                    mActivity.startActivity(intent);
                }
            });
        }

        /**
         * 初始化 距离四级考试时间的 TextView
         */
        public void initDayDistanceTV(){
            tvDayDistance = itemView.findViewById(R.id.tv_dayDistance);
            tvDate = itemView.findViewById(R.id.tv_date);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
            String sDate = dateFormat.format(new Date());
            tvDate.setText(sDate);

            //日差
            Date todayDate = new Date(Integer.parseInt(sDate.substring(0,4)),Integer.parseInt(sDate.substring(5,7)),Integer.parseInt(sDate.substring(8,10)));
            Date cet4_Date = new Date(2020,6,13);

            tvDayDistance.setText(DateUtils.calcDayOffset(todayDate,cet4_Date)+"");
        }

    }

    /**
     * 试卷，各种题型练习的入口
     * 提供并初始化 布局：layout_types_of_paper_question
     */
    private class PaperHolder extends RecyclerView.ViewHolder {

        // 四种题型模块
        private GridView gv_examItem;
        private List<Map<String, Object>> data_list = new ArrayList<>();;
        private SimpleAdapter sim_adapter;

        // 单词模块
        private TextView tvWord;
        // 真题实练模块
        private TextView tvTestPaper;

        public PaperHolder(@NonNull View itemView) {
            super(itemView);
            initViews();
        }

        private void initViews() {
            // 单词模块
            tvWord = itemView.findViewById(R.id.tv_exam_words);
            tvWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.startActivity(new Intent(mActivity, WordTestActivity.class));
                }
            });

            // 考试题型模块
            gv_examItem = itemView.findViewById(R.id.gv_examItem);
            initGridView();

            // 真题模块
            tvTestPaper = itemView.findViewById(R.id.tv_testpaper);
            tvTestPaper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mActivity,"真题",Toast.LENGTH_SHORT).show();
                    mActivity.startActivity(new Intent(mActivity, TranslateActivity.class));
                }
            });
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

            for(int i=0; i<icon.length; i++){
                Map<String, Object> map = new HashMap<>();
                map.put("image", icon[i]);
                map.put("text", iconName[i]);
                data_list.add(map);
            }

            //新建适配器
            String [] from ={"image","text"};
            int [] to = {R.id.image, R.id.text};
            sim_adapter = new SimpleAdapter(mActivity, data_list, R.layout.item, from, to);
            //配置适配器
            gv_examItem.setAdapter(sim_adapter);

            gv_examItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (position == 0){
                        mActivity.startActivity(new Intent(mActivity, ListeningActivity.class));
                    }
                    if (position == 1){
                        mActivity.startActivity(new Intent(mActivity, ReadingActivity.class));
                    }
                    if (position == 2){
                        mActivity.startActivity(new Intent(mActivity, TransActivity.class));
                    }
                    if (position == 3){
                        mActivity.startActivity(new Intent(mActivity, WritingActivity.class));
                    }
                }

            });

        }
    }

    /**
     * 其他的一些布局，比如推一些视频、课程等。
     * 提供并初始化 布局：layout_other
     */
    private class OtherHolder extends RecyclerView.ViewHolder {

        public OtherHolder(@NonNull View itemView) {
            super(itemView);

            LinearLayout parentView = (LinearLayout) itemView;
            ImageView ivOtherImage = new ImageView(mActivity);
            ivOtherImage.setImageDrawable(mActivity.getDrawable(R.drawable.app_icon));
            ivOtherImage.setBackground(mActivity.getDrawable(R.drawable.round_corner_bg2));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,400);
            params.leftMargin = 10;
            params.rightMargin = 10;
            params.bottomMargin = 10;
            ivOtherImage.setLayoutParams(params);

            parentView.addView(ivOtherImage,2);
        }
    }

    /**
     * 水平测试模块
     * 提供并初始化 布局：layout_level_test
     */
    private class LevelTestHolder extends RecyclerView.ViewHolder {
        private ImageView ivCloseLevelTest;

        public LevelTestHolder(@NonNull final View itemView) {
            super(itemView);
            ivCloseLevelTest = itemView.findViewById(R.id.iv_close_level_test);
            ivCloseLevelTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(1);
                }
            });
        }
    }

    /**
     * 如果 item 标识为999，则什么都不显示
     */
    private class NothingHolder extends RecyclerView.ViewHolder {

        public NothingHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void removeItem(int position){
        itemControllerList.remove(position);//删除数据源,移除集合中当前下标的数据
        notifyItemRemoved(position);//刷新被删除的地方
        notifyItemRangeChanged(position,getItemCount()); //刷新被删除数据，以及其后面的数据
    }

    public void addItem(int position){
        itemControllerList.add(position);//添加数据源,移除集合中当前下标的数据
        notifyItemRemoved(position);//刷新添加的地方
        notifyItemRangeChanged(position,getItemCount()); //刷新添加的数据，以及其后面的数据
    }
}
