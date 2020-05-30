package com.example.beans;

import androidx.annotation.Nullable;

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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null){
            return false;
        }
        if (obj == this){
            return true;
        }
        CollectedListening c = (CollectedListening) obj;
        return c.testPaperIndex == this.testPaperIndex && c.questionType == this.questionType;
    }
}
