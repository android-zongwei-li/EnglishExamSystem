package com.example.utils.testPaperUtils;

import android.content.Context;
import android.util.Log;

import com.example.beans.TestPaper;
import com.example.myapplication.R;
import com.example.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类 用于构建TestPaper，提供给app使用
 */
public class TestPaperFactory {
    private static final String TAG = "TestPaperFactory";

    // 创建 SingleObject 的一个对象
    private static TestPaperFactory testPaperFactory = new TestPaperFactory();
    // 让构造函数为 private，这样该类就不会被实例化
    private TestPaperFactory(){

    }
    // 获取唯一可用的对象
    public static TestPaperFactory getInstance(){
        return testPaperFactory;
    }

    private boolean isInit = false; // 标识 是否已经初始化，默认false

    private int[] word = {R.raw.cet4_2016_06_01,R.raw.cet4_2016_06_02,R.raw.cet4_2016_06_03,
            R.raw.cet4_2016_12_01,R.raw.cet4_2016_12_02,R.raw.cet4_2016_12_03,
            R.raw.cet4_2017_06_01,R.raw.cet4_2017_06_02,R.raw.cet4_2017_06_03,
            R.raw.cet4_2017_12_01,R.raw.cet4_2017_12_02,R.raw.cet4_2017_12_03,
            R.raw.cet4_2018_06_01,R.raw.cet4_2018_06_02,R.raw.cet4_2018_06_03,
            R.raw.cet4_2018_12_01,R.raw.cet4_2018_12_02,R.raw.cet4_2018_12_03,
            R.raw.cet4_2019_06_01,R.raw.cet4_2019_06_02,R.raw.cet4_2019_06_03};   // 数据源

    private List<TestPaperFromWord> testPaperList = new ArrayList<>();  // 所有试卷
    private List<TestPaperFromWord.TestPaperInfo> allTestPaperInfo = new ArrayList<>();  // 所有试卷-信息
    private List<TestPaperFromWord.WritingFormWord> allTestPaperWriting = new ArrayList<>();  // 所有试卷-写作
    private List<TestPaperFromWord.ListeningFormWord> allTestPaperListening = new ArrayList<>();  // 所有试卷-听力
    private List<TestPaperFromWord.ReadingFormWord> allTestPaperReading = new ArrayList<>();  // 所有试卷-阅读
    private List<TestPaperFromWord.TranslationFormWord> allTestPaperTranslation = new ArrayList<>();  // 所有试卷-翻译

    private List<String> listeningAndReadingAnswer = new ArrayList<>();//听力和阅读的答案

    /**
     * 负责数据的初始化
     * 即 —— 把 word -》testPaper对象，并存入testPaperList
     * 只初始化一次，保证多次调用也只初始一次
     * @param context
     */
    public void initData(Context context){

        if(!isInit){

            WordUtils wordUtils = new WordUtils(context);
            // 初始化听力和阅读的答案
            String allListeningAndReadingAnswer = wordUtils.readWord(R.raw.listening_and_reading_answer).trim();
            int a = allListeningAndReadingAnswer.indexOf("答案");//*第一个出现的索引位置
            while (a != -1) {
                int aCopy = a;
                a = allListeningAndReadingAnswer.indexOf("答案", a + 1);//*从这个索引往后开始第一个出现的位置
                if (a != -1){
                    listeningAndReadingAnswer.add(allListeningAndReadingAnswer.substring(aCopy,a));//除最后一个
                }
            }
            listeningAndReadingAnswer.add(allListeningAndReadingAnswer.substring(
                    allListeningAndReadingAnswer.lastIndexOf("答案"),allListeningAndReadingAnswer.length()-1));//最后一个

            for (int i = 0; i < word.length; i++){
                LogUtils.v("第几套："+i,"di  "+i+"套");
                List<String> answerList = new ArrayList<>();
                //对答案做进一步的提取
                String answer = listeningAndReadingAnswer.get(i);
                for (int j = 1; j < 55; j++){   // [1,54] 题
                    String choice = answer.substring(answer.indexOf(j+"."),answer.indexOf((j+1)+".")).trim();
                    answerList.add(choice.substring(choice.length()-1));
                }
                String choice55 = answer.substring(answer.indexOf("55.")).trim();
                LogUtils.v("choice55",choice55);
                answerList.add(choice55.substring(choice55.length()-1));//55题

                TestPaperFromWord testPaper = new TestPaperFromWord(context,word[i]);
                testPaper.setListeningAndReadingAnswer(answerList);
                LogUtils.v("listeningAndReading",testPaper.getListeningAndReadingAnswer().toString());
                testPaperList.add(testPaper);

                allTestPaperInfo.add(testPaper.getTestPaperInfo());
                allTestPaperWriting.add(testPaper.getWritingFormWord());
                allTestPaperListening.add(testPaper.getListeningFormWord());
                allTestPaperReading.add(testPaper.getReadingFormWord());
                allTestPaperTranslation.add(testPaper.getTranslationFormWord());
                LogUtils.v("第几套完成："+i,"di  "+i+"套");
            }

            isInit = true;

            LogUtils.v(TAG+"-初始数据","数据初始化完成");

            return;
        }

        LogUtils.v(TAG+"-初始数据","数据已经初始过了,直接调用：getTestPaperList()获取 ");

    }

    public List<TestPaperFromWord> getTestPaperList() {
        return testPaperList;
    }

    public List<TestPaperFromWord.TestPaperInfo> getAllTestPaperInfo() {
        return allTestPaperInfo;
    }

    public List<TestPaperFromWord.WritingFormWord> getAllTestPaperWriting() {
        return allTestPaperWriting;
    }

    public List<TestPaperFromWord.ListeningFormWord> getAllTestPaperListening() {
        return allTestPaperListening;
    }

    public List<TestPaperFromWord.ReadingFormWord> getAllTestPaperReading() {
        return allTestPaperReading;
    }

    public List<TestPaperFromWord.TranslationFormWord> getAllTestPaperTranslation() {
        return allTestPaperTranslation;
    }
}
