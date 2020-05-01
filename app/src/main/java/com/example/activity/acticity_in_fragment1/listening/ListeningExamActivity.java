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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.activity.acticity_in_fragment1.reading.ChooseWordActivity;
import com.example.activity.base.BaseAppCompatActivity;
import com.example.beans.Question;
import com.example.myapplication.R;
import com.example.utils.LogUtils;
import com.example.utils.testPaperUtils.TestPaperFactory;
import com.example.view.CommonControlBar;
import com.example.view.SimpleAudioPlayer;
import com.example.view.topbar.TopBar;

import java.util.ArrayList;
import java.util.List;

public class ListeningExamActivity extends BaseAppCompatActivity {
    private static final String TAG = "ListeningExamActivity";
    public static final String QUESTION_TYPE = "questionType";  // 题型
    public static final String TESTPAPER_INDEX = "testPaperIndex";  //试卷序号

    private static final String titles[] = {"短篇新闻","情景对话","听力文章"};

    // 选词填空：index == 0
    // 快速阅读：index == 1
    // 仔细阅读：index == 2
    private int index;  // 题型标识
    private int testPaperIndex; //试卷序号（第几套）

    private CommonControlBar controlBar;
    private ListView lv_listening;
    private TextView tv_listening;//听力题目
    private RadioGroup rg;//题目选项
    private SimpleAudioPlayer player;

    private AlertDialog alertDialog;
    private TextView tv_dialog_title,tv_dialog_content;// dialog中的 title 和 content
    private Button btn_dialog_determine,btn_dialog_cancel;//dialog中的按钮

    private List<Integer> checkedIdList = new ArrayList<>();//存储选中的id
    List<Integer> rightChoices = new ArrayList<>();// 正确答案，1，2，3，4对应A,B,C,D

    // 文章
    String title;
    private List<Question> listeningQuestions; // 听力 25 道，8篇
    private List<Question> questionList;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {    // 交卷，刷新UI
                controlBar.setVisibility(View.GONE);
                player.setVisibility(View.GONE);

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
                        int checkedIndex = question.getCurrentAnswer();

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

                            if (i == checkedIndex-1){   //checkedIndex:[1,4]
                                rb.setTextColor(getResources().getColor(R.color.wrong_answer));
                            }
                            if (i == rightChoices.get(position)){
                                rb.setTextColor(getResources().getColor(R.color.right_answer));
                            }
                        }

                        return convertView;
                    }
                });

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_news);

        initData();

        //
        TopBar topBar = findViewById(R.id.topBar);
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
                int[] unFinishedQuestionsIndex = new int[questionList.size()];   //记录未完成题目的序号
                for (int i = 0; i < questionList.size(); i++){
                    int checkedIndex = questionList.get(i).getCurrentAnswer();
                    checkedIdList.add(checkedIndex);

                    if (checkedIndex == -1){    //未选择任何项
                        isFnished = false;
                        unFinishedQuestionsIndex[i] = i;
                    }
                }

                if (isFnished){
                    tv_dialog_title.setText("全部完成");
                    tv_dialog_content.setText("是否交卷？");
                }else {
                    tv_dialog_title.setText("第 "+(unFinishedQuestionsIndex[0]+1)+" 还未完成");
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
                        //checked 取值范围：(1、2、3、4)*4
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
        player.setResourceId(R.raw.cet4_201806_01);

    }

    /**
     *  初始数据
     */
    private void initData(){
        // 获取 题目信息：题型、第几套
        Intent intent = getIntent();
        index = intent.getIntExtra(ChooseWordActivity.QUESTION_TYPE,0);
        testPaperIndex = intent.getIntExtra(ChooseWordActivity.TESTPAPER_INDEX,0);
        Toast.makeText(this,testPaperIndex+"",Toast.LENGTH_LONG).show();

        TestPaperFactory testPaperFactory = TestPaperFactory.getInstance();
        listeningQuestions = testPaperFactory.getAllTestPaperListening().get(testPaperIndex).getListeningQuestionList();
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

}
