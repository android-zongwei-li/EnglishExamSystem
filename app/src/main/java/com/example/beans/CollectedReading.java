package com.example.beans;

/**
 * Created by: lzw.
 * Date: 2020/5/3
 * Time: 19:05
 * Description：
 * 1、
 */
public class CollectedReading {
    private int testPaperIndex;//第几套试题
    private int questionType;   //题型

    public CollectedReading(int testPaperIndex, int questionType) {
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
