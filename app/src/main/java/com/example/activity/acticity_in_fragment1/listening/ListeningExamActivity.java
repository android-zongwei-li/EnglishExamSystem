package com.example.activity.acticity_in_fragment1.listening;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.MyApplication;
import com.example.activity.acticity_in_fragment1.reading.ReadingExamActivity;
import com.example.activity.base.BaseAppCompatActivity;
import com.example.beans.CollectedListening;
import com.example.beans.Question;
import com.example.myapplication.R;
import com.example.utils.AccountManager;
import com.example.utils.LogUtils;
import com.example.utils.MySqlDBOpenHelper;
import com.example.utils.ToastUtils;
import com.example.utils.testPaperUtils.TestPaperFactory;
import com.example.utils.testPaperUtils.TestPaperFromWord;
import com.example.view.CommonControlBar;
import com.example.view.SimpleAudioPlayer;
import com.example.view.topbar.TopBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * 注：交卷后的页面最好标识出未做的题
 * radio button最好不能再选择
 * 在提供一个返回主页的选项，应该要在topbar中增加方法
 */
public class ListeningExamActivity extends BaseAppCompatActivity {
    public static final String QUESTION_TYPE = "questionType";  // 题型
    public static final String TEST_PAPER_INDEX = "testPaperIndex";  //试卷序号

    private static final int[] titles = {R.string.listening_short_news,
            R.string.listening_conversation,R.string.listening_passages};

    // 选词填空：index == 0
    // 快速阅读：index == 1
    // 仔细阅读：index == 2
    private int index;  // 题型标识
    private int testPaperIndex; //试卷序号（第几套）,这是总的试题序号
    private String year;
    private String month;
    private String whichIndex;//这个变量记录，当年试题的第几套，有1、2、3

    private TopBar topBar;
    private CommonControlBar controlBar;
    private ListView lv_listening;
    private TextView tv_listening;//听力题目
    private RadioGroup rg;//题目选项
    private SimpleAudioPlayer player;

    private AlertDialog alertDialog;
    private TextView tv_dialog_title,tv_dialog_content;// dialog中的 title 和 content
    private Button btn_dialog_determine,btn_dialog_cancel;//dialog中的按钮

    private List<Integer> checkedIdList = new ArrayList<>();//存储选中的id
    private List<Integer> rightChoices = new ArrayList<>();// 正确答案，1，2，3，4对应A,B,C,D

    // 文章
    private String title;
    private List<Question> listeningQuestions; // 听力 25 道，8篇
    private List<Question> questionList;

    private CollectedListening listening = null;
    // 记录是否已经收藏
    private boolean isCollected = false;

    //记录未完成题目的序号
    ArrayList<Integer> unFinishedQuestionsIndex = new ArrayList<>();
    //记录选择的答案，有 -1、1、2、3、0
    ArrayList<Integer> checkedAnswers = new ArrayList<>();

    //已收藏听力题
    private ArrayList<CollectedListening> collectedListening = new ArrayList<>();

    private AccountManager am;
    private String telephone;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {    // 交卷，刷新UI
                topBar.setRighttIsVisable(false);
                controlBar.setVisibility(View.GONE);
                player.setVisibility(View.GONE);
                LinearLayout ll_result_statistics = findViewById(R.id.ll_result_statistics);
                ll_result_statistics.setVisibility(View.VISIBLE);
                TextView tv_time_used = ll_result_statistics.findViewById(R.id.tv_time_used);
                TextView tv_correct_percent = ll_result_statistics.findViewById(R.id.tv_correct_percent);

                lv_listening.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return questionList.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return null;
                    }

                    @Override
                    public long getItemId(int position) {
                        return 0;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        Question question = questionList.get(position);
                        int checkedIndex = question.getCurrentAnswer();//1、2、3、0

                        convertView = View.inflate(ListeningExamActivity.this,R.layout.listening_listview_item,null);

                        tv_listening = convertView.findViewById(R.id.tv_listening);
                        tv_listening.setText("Q"+(position+1)+":");

                        rg = convertView.findViewById(R.id.rg_listening_choices);
                        rg.setClickable(false);

                        String choices[] = {questionList.get(position).getChoiceA(),
                                questionList.get(position).getChoiceB(),
                                questionList.get(position).getChoiceC(),
                                questionList.get(position).getChoiceD()};
                        RadioButton rb;
                        for (int i = 0; i < 4; i++){
                            rb = (RadioButton) rg.getChildAt(i);
                            rb.setText(choices[i]);

                            // 最后一个选项，特殊处理一下，它的序号为0，以后有时间看一下为什么会这样
                            if (i == 3){
                                if (checkedIndex == 0){ //表示选择了第四个答案
                                    rb.setTextColor(getResources().getColor(R.color.wrong_answer));
                                }
                            }else {
                                // 前三个选项。checkedIndex:1、2、3
                                if (i == checkedIndex-1){
                                    rb.setTextColor(getResources().getColor(R.color.wrong_answer));
                                }
                            }

                            if (i == rightChoices.get(position)){
                                rb.setTextColor(getResources().getColor(R.color.right_answer));
                            }
                        }

                        return convertView;
                    }
                });

                //未完成题目个数
                int unFinishedNumber = unFinishedQuestionsIndex.size();
                //总题数
                int totalNumber = questionList.size();
                // 正确题数
                int rightNumber = 0;

                for (int i = 0; i < questionList.size(); i++){
                    int currentAnswer = questionList.get(i).getCurrentAnswer();
                    checkedAnswers.add(currentAnswer);
                }

                int questionIndex = 0; //记录当前题号，每个题只比较一次
                //isTrue ：-1、1、2、3、0  未做、A、B、C、D
                for (int isTrue : checkedAnswers){
                    LogUtils.i("选择的答案",isTrue+"");
                    // 和答案对齐
                    if (isTrue == 0){
                        isTrue = 3;
                    }else if (isTrue == -1){
                        //不变
                    }else{
                        // 1、2、3
                        // 变成 0、1、2  和   A、B、C对应
                        isTrue = isTrue - 1;
                    }

                    //rightAnswer：0、1、2、3  A、B、C、D
                    if (isTrue == rightChoices.get(questionIndex)){
                        rightNumber++;
                    }

                    // 下一题
                    questionIndex++;
                }

                //准确率
                double correctPercent = rightNumber*100/(double)totalNumber;
                LogUtils.i("correctPercent",correctPercent+"");
                double result = round(correctPercent,2);
                tv_correct_percent.setText("正确率："+ result +"%");

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_news);

        initData();

        am = AccountManager.getInstance(getApplication());
        telephone = am.getTelephone();
        if (am.isOnline()){
            initCollectedListening(telephone);
        }

        //
        topBar = findViewById(R.id.topBar);
        topBar.setTitle(titles[index]);
        topBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                alertDialog = new AlertDialog.Builder(ListeningExamActivity.this).create();
                alertDialog.show();
                Window window = alertDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setContentView(R.layout.dialog_exit);
                tv_dialog_title = window.findViewById(R.id.tv_dialog_title);
                tv_dialog_content = window.findViewById(R.id.tv_dialog_content);
                btn_dialog_determine = window.findViewById(R.id.btn_determine);
                btn_dialog_cancel = window.findViewById(R.id.btn_cancel);
                btn_dialog_determine.setText("确定");
                btn_dialog_cancel.setText("返回");

                //判断是否有题目未完成
                boolean isFnished = true;  //是否全部完成

                for (int i = 0; i < questionList.size(); i++){
                    int checkedIndex = questionList.get(i).getCurrentAnswer();
                    checkedIdList.add(checkedIndex);

                    if (checkedIndex == -1){    //未选择任何项
                        isFnished = false;
                        //加 1 ，题目序号从一开始记录，是真实的题目序号
                        unFinishedQuestionsIndex.add(i+1);
                    }
                }

                if (isFnished){
                    tv_dialog_title.setText("全部完成");
                    tv_dialog_content.setText("是否交卷？");
                }else {
                    // 这里提示第一个未完成的题目
                    tv_dialog_title.setText("第 "+unFinishedQuestionsIndex.get(0)+" 题还未完成");
                    tv_dialog_content.setText("确定交卷？");
                }
                btn_dialog_determine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        //关闭弹窗，发送消息，刷新UI
                        handler.sendEmptyMessage(0);
                    }
                });
                btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkedIdList.clear();
                        alertDialog.dismiss();
                    }
                });

            }
        });

        //
        controlBar = findViewById(R.id.commonControlBar);
        final ImageView ivIConCollection = controlBar.findViewById(R.id.iv_icon_collection);
        ivIConCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (am.isOnline()){

                    if (!isCollected){    //默认为false，为收藏
                        isCollected = true;
                        ivIConCollection.setImageResource(R.drawable.icon_collection_after);
                        listening = new CollectedListening(testPaperIndex,index);
                        if (!collectedListening.contains(listening)){
                            collectedListening.add(listening);
                        }
                        Toast.makeText(ListeningExamActivity.this,"已收藏",Toast.LENGTH_SHORT).show();
                    }else {
                        isCollected = false;
                        ivIConCollection.setImageResource(R.drawable.icon_collection_befor);
                        if (collectedListening.contains(listening)){
                            collectedListening.remove(listening);
                        }
                        Toast.makeText(ListeningExamActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    ToastUtils.show("登录后可收藏题目");
                }

            }
        });

        //
        lv_listening = findViewById(R.id.lv_listening);
        lv_listening.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return questionList.size();
            }

            @Override
            public Object getItem(int position) {
                return questionList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final Question question = questionList.get(position);
                convertView = View.inflate(ListeningExamActivity.this,R.layout.listening_listview_item,null);;

                tv_listening = convertView.findViewById(R.id.tv_listening);
                tv_listening.setText("Q"+(position+1)+":");

                rg = convertView.findViewById(R.id.rg_listening_choices);
                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        //checked 取值范围：(1、2、3、0)*4
                        question.setCurrentAnswer(checkedId%4);   //记录下当前的选中项，默认为 -1
                    }
                });
                // 当调用getView的时候，要判断一下，之前这个position对应的题目是否已经选过了，
                // 选过的话，把选中项的选择状态设为true
                // 这里这样做的原因是，当下滑到上面的item全部出屏幕后，此item会被回收，导致视图消失，
                // 这里保证了在上滑到那个item时，它的状态会保存下来
                // question.getCurrentAnswer: -1,1,2,3,4
                if (question.getCurrentAnswer() != -1) {
                    int i = (question.getCurrentAnswer() - 1);

                    // 当选中最后一个选项时，i = -1，要重设为3
                    if (i == -1){
                        i = 3;
                    }

                    ((RadioButton) rg.getChildAt(i)).setChecked(true);
                }

                String[] choices = new String[]{question.getChoiceA(),
                        question.getChoiceB(),
                        question.getChoiceC(),
                        question.getChoiceD()};
                RadioButton rb;
                for (int i = 0; i < 4; i++){
                    rb = (RadioButton) rg.getChildAt(i);
                    rb.setText(choices[i]);
                }

                return convertView;
            }
        });

        //
        player = findViewById(R.id.simple_audio_player);
        //访问本地资源
        player.setUrl("http://192.168.43.68:8080/listening-audio/"+year+"-"+month+"-"+whichIndex+".mp3");
        //访问云资源
 //       player.setUrl("https://english-exam-system-resources.oss-cn-beijing.aliyuncs.com/listening-audio/"+year+"-"+month+"-"+whichIndex+".mp3");
    }

    /**
     *  初始数据
     */
    private void initData(){
        // 获取 题目信息：题型、第几套
        Intent intent = getIntent();
        index = intent.getIntExtra(ReadingExamActivity.QUESTION_TYPE,0);
        testPaperIndex = intent.getIntExtra(ReadingExamActivity.TEST_PAPER_INDEX,0);

        TestPaperFactory testPaperFactory = TestPaperFactory.getInstance();
        listeningQuestions = testPaperFactory.getAllTestPaperListening()
                .get(testPaperIndex)
                .getListeningQuestionList();
        // 答案部分
        List<String> listeningAnswer = testPaperFactory.getTestPaperList().get(testPaperIndex)
                .getListeningAndReadingAnswer().subList(0,25);
        LogUtils.i("listeningAnswer",listeningAnswer.toString());
        List<Integer> rightChoicesAll = new ArrayList<>();// 听力所有正确答案
        for (int i = 0; i < listeningAnswer.size(); i++){
            String letter = listeningAnswer.get(i); //答案A、B、C、D，转换成数字0，1，2，3
            switch (letter){
                case "A":
                    rightChoicesAll.add(0);
                    break;
                case "B":
                    rightChoicesAll.add(1);
                    break;
                case "C":
                    rightChoicesAll.add(2);
                    break;
                case "D":
                    rightChoicesAll.add(3);
                    break;
            }
        }

        // 获取年份、月份、第几套
        TestPaperFromWord.TestPaperInfo testPaperInfo = testPaperFactory
                .getAllTestPaperInfo().get(testPaperIndex);
        year = testPaperInfo.getYear();
        month = testPaperInfo.getMonth();
        whichIndex = testPaperInfo.getIndex();
        // 第三套卷，没有独立的听力资源，加载第二套
        // 这里的资源后面要和实际情况比对一下，
        // 看看每年具体的题目是怎么编排的
        if (Integer.valueOf(whichIndex) == 3){
            whichIndex = 2 + "";
        }


        switch (index){ //根据题型，初始化不同的数据
            case 0: // 2，2，3
                questionList = listeningQuestions.subList(0,7);
                rightChoices = rightChoicesAll.subList(0,7);
                break;
            case 1: // 4，4
                questionList = listeningQuestions.subList(7,15);
                rightChoices = rightChoicesAll.subList(7,15);
                break;
            case 2: // 3，3，4
                questionList = listeningQuestions.subList(15,25);
                rightChoices = rightChoicesAll.subList(15,25);
                break;
        }

    }

    public double round(Double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = null == v ? new BigDecimal("0.0") : new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 从map中取出数据：collectedListening(收藏的听力题)
     */
    private void initCollectedListening(final String telephone){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String,Object> map = MySqlDBOpenHelper.getUserInfoFormMySql(telephone);

                if (map != null){
                    String s = "";
                    for (String key : map.keySet()){
                        s += key + ":" + map.get(key) + "\n";
                    }
                    LogUtils.i("查询结果",s);

                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<CollectedListening>>(){}.getType();
                    if (map.get("collectedListening") != null){
                        collectedListening = gson.fromJson(map.get("collectedListening").toString(),type);
                    }
                }else {
                    ToastUtils.show(ListeningExamActivity.this,"查询结果为空");
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (am.isOnline()){

            Gson gson = new Gson();
            final String inputCollectedListening = gson.toJson(collectedListening);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Connection conn = MySqlDBOpenHelper.getConn();
                    String sql_insert = "update user set collectedListening=? " +
                            "where telephone = "+telephone;
                    try {

                        PreparedStatement pstm = conn.prepareStatement(sql_insert);
                        pstm.setString(1, inputCollectedListening);
                        pstm.executeUpdate();

                        if (pstm != null){
                            pstm.close();
                        }
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

    }
}
