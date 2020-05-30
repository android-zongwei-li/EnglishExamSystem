package com.example.activity.acticity_in_fragment1.reading;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.aigestudio.wheelpicker.WheelPicker;
import com.example.MyApplication;
import com.example.activity.acticity_in_fragment1.listening.ListeningExamActivity;
import com.example.activity.base.BaseAppCompatActivity;
import com.example.beans.CollectedListening;
import com.example.beans.CollectedReading;
import com.example.beans.CollectionBank;
import com.example.beans.Question;
import com.example.beans.Range;
import com.example.fragment.CarefullyReadingFragment;
import com.example.myapplication.R;
import com.example.utils.AccountManager;
import com.example.utils.LogUtils;
import com.example.utils.MySqlDBOpenHelper;
import com.example.utils.ToastUtils;
import com.example.utils.testPaperUtils.TestPaperFactory;
import com.example.utils.testPaperUtils.TestPaperFromWord;
import com.example.view.CommonControlBar;
import com.example.view.topbar.TopBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 注：
 * 仔细阅读，底部的textView的颜色变化还有一些问题
 * 仔细阅读，交卷后能不能选项颜色变一下，并且设置为不能再选择。
 */
public class ReadingExamActivity extends BaseAppCompatActivity {
    private final int[] titles = {R.string.reading_choose_word,
            R.string.reading_quickly,
            R.string.reading_carefully};
    public static final String QUESTION_TYPE = "questionType";  // 题型
    public static final String TEST_PAPER_INDEX = "testPaperIndex";  //试卷序号
    // 选词填空：index == 0
    // 快速阅读：index == 1
    // 仔细阅读：index == 2
    private int index;  // 题型标识
    private int testPaperIndex; //试卷序号（第几套）

    private TestPaperFromWord.ReadingFormWord.ChooseWordBean chooseWordBean;
    private TestPaperFromWord.ReadingFormWord.QuicklyReadingBean quicklyReadingBean;
    private TestPaperFromWord.ReadingFormWord.CarefulReadingBean carefulReadingBean;

    private CommonControlBar controlBar;
    // 文章显示框
    private TextView tvTitleDisplay;
    // 答案选择框（单选框）
    private AlertDialog answerAlertDialog;
    //
    private SlidingDrawer drawer;
    // drawer content
    private RecyclerView lvDrawerContent;
    // 选词填空
    private ViewPager viewPager;

    // 文章   选词填空、仔细阅读
    String title;
    List<String> chooseWordOptionalWord;//选词填空的可选词汇
    List<Question> quicklyReadingQuestions; // 快速阅读的问题
    List<Question> carefullyReadingQuestions; // 仔细阅读的问题

    // 快速阅读的段落序号
    List<String> passageLetters = Arrays.asList("空","A","B","C","D","E",
            "F","G","H","I","J",
            "K","L","M","N","O");

    //选词填空部分
    List<Range> ranges = new ArrayList<>();     // 记录 span 设置的范围
    List<String> answers = new ArrayList<>();   // 记录 range 范围中的字符

    // 正确答案-里面是答案对应的字母
    private List<String> rightChoices = new ArrayList<>();
    // 正确答案-单词
    private List<String> correctAnswer = new ArrayList<>();

    List<Map> carefullyReadingAnswer = new ArrayList<>();

    CollectedReading reading = null;
    // 记录是否已经收藏
    private boolean isCollected = false;

    //仔细阅读底部的textView，拿过来设置颜色，正确设置绿色，错误/没做设为红色
    private List<List<TextView>> questionsIndexTextViews = new ArrayList<>();

    //记录快速阅读选择的答案
    private Map<Integer,Integer> selectedItems = new HashMap<>();
    //记录用户选的答案
    private List<String> carefullyReadingUserAnswer = new ArrayList();
    //记录交卷后的结果，true or false
    private List<Boolean> carefullyReadingResult = new ArrayList();
    private List<TextView> userAnswerTV = new ArrayList<>();

    //已收藏听力题
    private ArrayList<CollectedReading> collectedReading = new ArrayList<>();

    private AccountManager am;
    private String telephone;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            QuicklyRVAdapter adapter = new QuicklyRVAdapter(1,quicklyReadingQuestions,carefullyReadingUserAnswer,rightChoices);
            lvDrawerContent.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_word);

        initData();

        initView();

    }

    /**
     *  初始化TextView控件，并配置初始数据
     */
    private void initData(){

        am = AccountManager.getInstance(getApplication());
        telephone = am.getTelephone();
        if (am.isOnline()){
            initCollectedWriting(telephone);
        }


        // 获取 题目信息：题型、第几套
        Intent intent = getIntent();
        index = intent.getIntExtra(ReadingExamActivity.QUESTION_TYPE,0);
        testPaperIndex = intent.getIntExtra(ReadingExamActivity.TEST_PAPER_INDEX,0);
        Toast.makeText(this,testPaperIndex+"",Toast.LENGTH_LONG).show();

        TestPaperFactory testPaperFactory = TestPaperFactory.getInstance();
        List<String> readingAnswer = testPaperFactory.getTestPaperList().get(testPaperIndex)
                .getListeningAndReadingAnswer().subList(25,55);
        LogUtils.i("listeningAnswer",readingAnswer.toString());
        switch (index){ //根据题型，初始化不同的数据
            case 0:
                chooseWordBean = testPaperFactory.getAllTestPaperReading().get(testPaperIndex).getChooseWordBean();
                title = chooseWordBean.getTitle();
                chooseWordOptionalWord = chooseWordBean.getOptionalWords();
                rightChoices = readingAnswer.subList(0,10);
                for (int i = 0; i < rightChoices.size(); i++){
                    String letter = rightChoices.get(i);
                    for (String s : chooseWordOptionalWord){
                        if (s.contains(letter+")")){
                            correctAnswer.add(s.trim());
                        }
                    }
                }
                break;
            case 1:
                quicklyReadingBean = testPaperFactory.getAllTestPaperReading().get(testPaperIndex).getQuicklyReadingBean();
                title = quicklyReadingBean.getTitle();
                quicklyReadingQuestions = quicklyReadingBean.getQuicklyReadQuestion1();
                rightChoices = readingAnswer.subList(10,20);
                break;
            case 2:
                carefulReadingBean = testPaperFactory.getAllTestPaperReading().get(testPaperIndex).getCarefulReadingBean();
                carefullyReadingQuestions = carefulReadingBean.getReadCarefullyQuestion1();
                rightChoices = readingAnswer.subList(20,30);
                break;
        }
    }

    private void initView(){
        //
        final TopBar topBar = findViewById(R.id.topBar);
        topBar.setTitle(titles[index]);
        topBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                controlBar.setVisibility(View.GONE);
                topBar.setRighttIsVisable(false);

                if (index == 0){    //选词填空交卷
                    TextView tv_correct_answer = findViewById(R.id.tv_correct_answer);
                    tv_correct_answer.setVisibility(View.VISIBLE);
                    tv_correct_answer.setText("参考答案\n"+correctAnswer.subList(0,4).toString()+"\n"+
                            correctAnswer.subList(4,8).toString()+"\n"+correctAnswer.subList(8,10));

                    // 选词填空
                    List<Range> rightRange = new ArrayList<>();
                    List<Range> wrongRange = new ArrayList<>();//错误选项的范围
                    for (int i = 0; i < answers.size(); i++){
                        if (rightChoices.get(i).charAt(0) != answers.get(i).charAt(0)){ // 答案错误，把选项颜色变红
                            wrongRange.add(ranges.get(i));
                            continue;
                        }
                        rightRange.add(ranges.get(i));
                    }
                    setResultSpan(title,rightRange,wrongRange);
                }

                //仔细阅读交卷
                if (index == 1){
                    LinearLayout ll_result_statistics = findViewById(R.id.ll_result_statistics);
                    ll_result_statistics.setVisibility(View.VISIBLE);
                    TextView tv_time_used = ll_result_statistics.findViewById(R.id.tv_time_used);
                    TextView tv_correct_percent = ll_result_statistics.findViewById(R.id.tv_correct_percent);

                    LogUtils.i("selectedItems",selectedItems.toString());
                    LogUtils.i("rightChoices",rightChoices.toString());

                    for (int i = 0; i < 10; i++){
                        //不包含，说明没做，给它加一个没做的标识(0)
                        if (!selectedItems.containsKey(i)){
                            selectedItems.put(i,0);
                        }
                    }
                    LogUtils.i("selectedItems",selectedItems.toString());

                    for (int i = 0; i < selectedItems.size(); i++){
                        //得到所选项序号，要转换为对应字母
                        int answerIndex = selectedItems.get(i);
                        //把选项(position)转化为字母
                        String answer = passageLetters.get(answerIndex);
                        carefullyReadingUserAnswer.add(answer);
                    }
                    LogUtils.i("carefullyReadingUserAnswer",carefullyReadingUserAnswer.toString());

                    //正确题数
                    int rightTotal = 0;
                    for (int i = 0; i < rightChoices.size(); i++){
                        //正确答案
                        String right = rightChoices.get(i);

                        //用户选择答案
                        if (i >= carefullyReadingUserAnswer.size()){
                            carefullyReadingUserAnswer.add("空");
                        }
                        String userAnswer = carefullyReadingUserAnswer.get(i);

                        if (right.equals(userAnswer)){
                            carefullyReadingResult.add(true);
                            rightTotal++;
                        }else {
                            carefullyReadingResult.add(false);
                        }

                    }

                    double correctPercent = rightTotal/(double)10;
                    double result = round(correctPercent,2);
                    tv_correct_percent.setText("正确率："+result*100+"%");

                    for (int i = 0; i < userAnswerTV.size(); i++){
                        TextView textView = userAnswerTV.get(i);
                        textView.setText("你的答案："+carefullyReadingUserAnswer.get(i));
                        if (!carefullyReadingResult.get(i)){
                            textView.setTextColor(getResources().getColor(R.color.wrong_answer));
                        }
                    }

                    //更新视图
                    mHandler.sendEmptyMessage(0);

                }

                if (index == 2){
                    topBar.setRighttIsVisable(false);
                    controlBar.setVisibility(View.GONE);
                    //禁用后颜色会变浅
//                    disableRadioGroup((RadioGroup) findViewById(R.id.rg_answer1));

                    LinearLayout ll_result_statistics = findViewById(R.id.ll_result_statistics);
                    ll_result_statistics.setVisibility(View.VISIBLE);
                    TextView tv_time_used = ll_result_statistics.findViewById(R.id.tv_time_used);
                    TextView tv_correct_percent = ll_result_statistics.findViewById(R.id.tv_correct_percent);

                    //存储最终的结果
                    List<Boolean> answerList = new ArrayList<>();
                    // 获取carefullyReading做的答案
                    for (int i = 0; i < carefullyReadingAnswer.size(); i++){
                        Map answer = carefullyReadingAnswer.get(i);

                        List passageAnswer; // 记录每篇文章的答案
                        if (i == 0){
                            passageAnswer = rightChoices.subList(0,5);
                        }else {
                            passageAnswer = rightChoices.subList(5,10);
                        }

                        for (int j = 0; j < passageAnswer.size(); j++){

                            // 判断当前的题目是否做了,true表示做了
                            if (answer.containsKey(i)){
                                int userAnswer = (int) answer.get(i);
                                switch (userAnswer){
                                    case 0 :
                                        answerList.add(passageAnswer.get(j).equals("A"));
                                        break;
                                    case 1 :
                                        answerList.add(passageAnswer.get(j).equals("B"));
                                        break;
                                    case 2 :
                                        answerList.add(passageAnswer.get(j).equals("C"));
                                        break;
                                    case 3 :
                                        answerList.add(passageAnswer.get(j).equals("D"));
                                        break;
                                }
                            }else {
                                answerList.add(false);
                            }
                        }

                    }

                    //统计answerList中有多少true，计算准确率
                    int rightTotal = 0;
                    int index = 0;
                    for (boolean isTrue : answerList){
                        if (isTrue){
                            rightTotal++;
                        }else {
                            if (index < 5){
                                TextView textView = questionsIndexTextViews.get(0).get(index);
                                textView.setTextColor(getResources().getColor(R.color.wrong_answer));
                            }else {
                                TextView textView = questionsIndexTextViews.get(1).get(index%5);
                                textView.setTextColor(getResources().getColor(R.color.wrong_answer));
                            }
                        }
                        index = index + 1;
                    }

                    tv_time_used.setText("用时："+0.0);

                    double correctPercent = rightTotal/(double)10;
                    double result = round(correctPercent,2);
                    tv_correct_percent.setText("正确率："+ result*100 +"%");

                }

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
                        reading = new CollectedReading(testPaperIndex,index);
                        if (!collectedReading.contains(reading)){
                            collectedReading.add(reading);
                        }
                        Toast.makeText(ReadingExamActivity.this,"已收藏",Toast.LENGTH_SHORT).show();
                    }else {
                        isCollected = false;
                        ivIConCollection.setImageResource(R.drawable.icon_collection_befor);
                        if (collectedReading.contains(reading)){
                            collectedReading.remove(reading);
                        }
                        Toast.makeText(ReadingExamActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    ToastUtils.show("登录后可使用收藏功能");
                }

            }
        });

        //
        tvTitleDisplay = findViewById(R.id.tv_title_display);
        tvTitleDisplay.setText(title);
        tvTitleDisplay.setBackground(null);

        if (index == 0){    // 选词填空 TextView 的设置
            initTextView();
        }

        // 快速阅读，显示右侧抽屉按钮
        if (index == 1){
            tvTitleDisplay.setPadding(0,0,40,0);
            drawer = findViewById(R.id.drawer);
            drawer.setVisibility(View.VISIBLE);
            drawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
                @Override
                public void onDrawerOpened() {
                    drawer.setClickable(true);
                }
            });
            drawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
                @Override
                public void onDrawerClosed() {
                    drawer.setClickable(false);
                }
            });

            initDrawerContent();
        }

        if (index == 2){
            findViewById(R.id.rl_exam).setVisibility(View.GONE);
            viewPager = findViewById(R.id.vp_exam);
            viewPager.setVisibility(View.VISIBLE);
            viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @NonNull
                @Override
                public Fragment getItem(int position) {
                    List<Question> questionList;
                    if (position == 0) {    // 第一个fragment，显示第一篇文章和题目
                        title = carefulReadingBean.getPassageOne();
                        questionList = carefullyReadingQuestions.subList(0,5);
                    }else {
                        title = carefulReadingBean.getPassageTwo();
                        questionList = carefullyReadingQuestions.subList(5,carefullyReadingQuestions.size());
                    }

                    final CarefullyReadingFragment carefullyReadingFragment =
                            new CarefullyReadingFragment(title, questionList);
                    carefullyReadingAnswer.add(carefullyReadingFragment.questionMap);//拿到答案
                    questionsIndexTextViews.add(carefullyReadingFragment.questionsIndexTextViews);
                    LogUtils.i("carefullyReadingAnswer",carefullyReadingAnswer.toString());
                    return carefullyReadingFragment;
                }

                @Override
                public int getCount() {
                    return 2;   //共两个fragment
                }
            });
        }
    }

    /**
     * 初始化：SlidingDrawer的content布局
     */
    private void initDrawerContent(){

        lvDrawerContent = drawer.findViewById(R.id.lv_drawer_content);
        lvDrawerContent.setLayoutManager(new
                LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        QuicklyRVAdapter adapter = new QuicklyRVAdapter(0,quicklyReadingQuestions,passageLetters,rightChoices);
        lvDrawerContent.setAdapter(adapter);
        selectedItems = adapter.getSelectedItems();
    }

    //选词填空部分
    /**
     *  设置文章的题目位置的颜色以及点击事件
     */
    private void initTextView(){

        // 找到 题目序号，这些序号要用答案来替换
        StringBuilder sb = new StringBuilder(title);
        Pattern pattern = Pattern.compile("[2-3][0-9]");
        Matcher findMatcher = pattern.matcher(sb);
        while(findMatcher.find()) {      //如果检索到则为true

            int i = findMatcher.start();    // 每次检索到的下标
            Range range = new Range(i,i+2);
            ranges.add(range);

            String answer = findMatcher.group();
            answers.add(answer);
        }

        // 初始化设置
        setSpan(title,ranges);
    }

    /**
     * 设置 textView 的span   ——  选词填空的
     * @param title     //文章
     * @param ranges    //需要设置的范围
     */
    private void setSpan(String title,List<Range> ranges){
        // 根据题目序号，设置span
        SpannableString content = new SpannableString(title);
        for (int i = 0; i < ranges.size(); i++){
            MyClickableSpan myClickableSpan = new MyClickableSpan(i);
            content.setSpan(myClickableSpan, ranges.get(i).getStart(), ranges.get(i).getEnd(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 设置此方法后，点击事件才能生效
        tvTitleDisplay.setMovementMethod(LinkMovementMethod.getInstance());

        tvTitleDisplay.setText(content);
    }

    /**
     * 标识出错误答案
     * @param title
     * @param wrongRanges
     */
    private void setResultSpan(String title,List<Range> rightRanges,List<Range> wrongRanges){
        LogUtils.i("wrongRanges",wrongRanges.toString());
        // 根据题目序号，设置span
        SpannableString content = new SpannableString(title);
        //先把所有span设置为 绿色
        for (int i = 0; i < rightRanges.size(); i++){
            //这句话需要在for循环里面，不然自由最后一个span有效
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.right_answer));
            content.setSpan(colorSpan, rightRanges.get(i).getStart(), rightRanges.get(i).getEnd(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 再标识出错误答案
        for (int i = 0; i < wrongRanges.size(); i++){
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.wrong_answer));
            content.setSpan(colorSpan, wrongRanges.get(i).getStart(), wrongRanges.get(i).getEnd(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tvTitleDisplay.setText(content);
    }

    private class MyClickableSpan extends ClickableSpan {
        // 题目序号
        int answerIndex;

        public MyClickableSpan(int answerIndex){
            this.answerIndex = answerIndex;
        }

        @Override
        public void onClick(View widget) {
            showAnswerAlertDialog(widget,answerIndex);
        }
    }

    public void showAnswerAlertDialog(final View view, final int answerIndex){
        final String[] choices = new String[chooseWordOptionalWord.size()]; //选词填空，可选词汇
        for (int i = 0; i < choices.length; i++){
            // 现在 choices 中的单词后面，是含有一些 空格的，从word中取出来的时候并没有剔除，在这里做一下处理,调用 trim()即可
            choices[i] = chooseWordOptionalWord.get(i).trim();
        }

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setSingleChoiceItems(choices, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //注：coClick中的 i 用于控制 choices 的取值；answerIndex决定 answers 和 ranges 的取值

                TextView tv = (TextView) view;
                //更新答案,这里需注意，序号是answerIndex，而不是 i
                int answerLength = answers.get(answerIndex).length();   //记录下将被替换的单词的长度，用于后面计算range的该变量
                String newTitle = tv.getText().toString().replace(answers.get(answerIndex),choices[i]);
                title = newTitle;
                answers.set(answerIndex,choices[i]);
                //更新需要设置span的区间
                //新的答案设置以后，这个题目前面的range是不受影响的，但后面的range全部受到影响，需做处理
                //首先设置当前 答案 的range start保持不变，end改变
                ranges.get(answerIndex).setEnd(ranges.get(answerIndex).getStart()+choices[i].length());

                /**
                 * 这里说明一下：
                 * 当替换了一个单词的时候，后面 range 的改变量 是 两个单词长度之差，而不是替换单词的长度
                 */
                int changdNum = choices[i].length() - answerLength; // 记录 range 的改变值（增量），即 改变的字符长度，可正可负
                //设置，后面 答案 的range
                for (int j = answerIndex+1; j < ranges.size(); j++){

                    ranges.get(j).setStart(ranges.get(j).getStart()+changdNum);
                    ranges.get(j).setEnd(ranges.get(j).getEnd()+changdNum);
                }
                //更新span
                setSpan(title,ranges);

                answerAlertDialog.dismiss();
            }
        });

        answerAlertDialog = alertBuilder.create();
        answerAlertDialog.show();

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = answerAlertDialog.getWindow().getAttributes();  //获取对话框当前的参数值
        answerAlertDialog.getWindow().setDimAmount(0);//设置昏暗度为0
        p.height = (int) (d.getHeight() * 0.6);   //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.8);    //宽度设置为屏幕的0.5
        answerAlertDialog.getWindow().setAttributes(p);     //设置生效

    }

    /**
     * double保留scale位小数
     * @param v
     * @param scale
     * @return
     */
    public double round(Double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = null == v ? new BigDecimal("0.0") : new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 启用RadioGroup
     * @param radioGroup
     */
    public void enableRadioGroup(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(true);
        }
    }

    /**
     * 禁用RadioGroup
     * @param radioGroup
     */
    public void disableRadioGroup(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(false);
        }
    }

    /**
     * 从map中取出数据：collectedWriting
     */
    private void initCollectedWriting(final String telephone){

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
                    Type type = new TypeToken<ArrayList<CollectedReading>>(){}.getType();
                    if (map.get("collectedReading") != null){
                        collectedReading = gson.fromJson(map.get("collectedReading").toString(),type);
                    }
                }else {
                    ToastUtils.show(ReadingExamActivity.this,"查询结果为空");
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (am.isOnline()){

            Gson gson = new Gson();
            final String inputCollectedReading = gson.toJson(collectedReading);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Connection conn = MySqlDBOpenHelper.getConn();
                    String sql_insert = "update user set collectedReading=? " +
                            "where telephone = "+telephone;
                    try {

                        PreparedStatement pstm = conn.prepareStatement(sql_insert);
                        pstm.setString(1, inputCollectedReading);
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
