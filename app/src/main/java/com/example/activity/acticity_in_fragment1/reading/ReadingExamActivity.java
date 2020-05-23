package com.example.activity.acticity_in_fragment1.reading;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aigestudio.wheelpicker.WheelPicker;
import com.example.activity.base.BaseAppCompatActivity;
import com.example.beans.CollectedReading;
import com.example.beans.CollectionBank;
import com.example.beans.Question;
import com.example.beans.Range;
import com.example.fragment.CarefullyReadingFragment;
import com.example.myapplication.R;
import com.example.utils.LogUtils;
import com.example.utils.testPaperUtils.TestPaperFactory;
import com.example.utils.testPaperUtils.TestPaperFromWord;
import com.example.view.CommonControlBar;
import com.example.view.topbar.TopBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private ListView lvDrawerContent;
    // 选词填空
    private ViewPager viewPager;

    // 文章   选词填空、仔细阅读
    String title;
    List<String> chooseWordOptionalWord;//选词填空的可选词汇
    List<Question> quicklyReadingQuestions; // 快速阅读的问题
    List<Question> carefullyReadingQuestions; // 仔细阅读的问题

    // 快速阅读的段落序号
    List<String> passageLetters = Arrays.asList("A","B","C","D","E",
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

    //快速阅读部分
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

                if (index == 1){

                }

                if (index == 2){
                    // 获取carefullyReading做的答案
                    for (int i = 0; i < carefullyReadingAnswer.size(); i++){
                        Map answer = carefullyReadingAnswer.get(i);
                        List<String> answerList = new ArrayList<>();
                        LogUtils.i("carefullyReading",answer.toString());
                        for (int j = 0; j < answer.size(); j++){
                            switch ((int)answer.get(j)){
                                case -1:    // 没选
                                    answerList.add("-1");
                                    break;
                                case 1 :
                                    answerList.add("A");
                                    break;
                                case 2 :
                                    answerList.add("B");
                                    break;
                                case 3 :
                                    answerList.add("C");
                                    break;
                                case 0 :
                                    answerList.add("D");
                                    break;
                            }
                        }
                        LogUtils.i("answerList",answerList.toString());
                    }
                }

            }
        });

        //
        controlBar = findViewById(R.id.commonControlBar);
        final ImageView ivIConCollection = controlBar.findViewById(R.id.iv_icon_collection);
        ivIConCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionBank collectionBank = CollectionBank.getInstance();
                if (!isCollected){    //默认为false，为收藏
                    isCollected = true;
                    ivIConCollection.setImageResource(R.drawable.icon_collection_after);
                    reading = new CollectedReading(testPaperIndex,index);
                    collectionBank.add(reading);
                    Toast.makeText(ReadingExamActivity.this,"已收藏",Toast.LENGTH_SHORT).show();
                    LogUtils.i("collectedReading",collectionBank.getCollectedReading().toString());
                }else {
                    isCollected = false;
                    ivIConCollection.setImageResource(R.drawable.icon_collection_befor);
                    collectionBank.remove(reading);
                    Toast.makeText(ReadingExamActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                    LogUtils.i("collectedReading",collectionBank.getCollectedReading().toString());
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

        if (index == 1){    // 快速阅读，显示右侧抽屉按钮
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
        lvDrawerContent.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return quicklyReadingQuestions.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup viewGroup) {
                View view;
                /**对ListView的优化，convertView为空时，创建一个新视图；
                 * convertView不为空时，代表它是滚出,
                 * 放入Recycler中的视图,若需要用到其他layout，
                 * 则用inflate(),同一视图，用findViewBy()
                 * **/
                if(convertView == null )
                {
                    LayoutInflater inflater = ReadingExamActivity.this.getLayoutInflater();
                    view = inflater.inflate(R.layout.drawer_content_list_item,null);
                    //view = View.inflate(getBaseContext(),R.layout.item,null);
                }
                else
                {
                    view = convertView;
                }

                TextView textView = view.findViewById(R.id.tv_drawer_content_item);
                textView.setText(quicklyReadingQuestions.get(position).getQuestion());

                WheelPicker wheel = view.findViewById(R.id.wheel);
                wheel.setData(passageLetters);
                wheel.setIndicator(true);
                wheel.setIndicatorColor(R.color.exit_dialog_orange);
                wheel.setVisibleItemCount(3);
                wheel.setItemTextColor(R.color.colorBlack);
                wheel.setSelectedItemTextColor(R.color.colorAccent);
                wheel.setCurtain(true);
                wheel.setAtmospheric(true);
                wheel.isCurved();

                return view;

            }
        });
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

    class MyClickableSpan extends ClickableSpan {
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

}
