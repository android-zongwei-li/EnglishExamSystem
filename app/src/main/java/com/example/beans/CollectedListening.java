package com.example.beans;

/**
 * Created by: lzw.
 * Date: 2020/5/3
 * Time: 18:55
 * Description：
 * 1、记录收藏题目的信息
 */
public class CollectedListening {
    private int testPaperIndex;//第几套试题
    private int questionType;   //题型

    public CollectedListening(int testPaperIndex, int questionType) {
        this.testPaperIndex = testPaperIndex;
        this.questionType = questionType;
    }

    public int getTestPaperIndex() {
        return testPaperIndex;
    }

    public int getQuestionType() {
        return questionType;
    }
}
