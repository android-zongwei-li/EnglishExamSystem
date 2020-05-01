package com.example.beans;

import java.util.List;

/**
 * 问题实体类
 */
public class Question {

    private String question;// 问题 听力、仔细阅读、选词填空都有

    // 听力、仔细阅读
    private String choiceA;
    private String choiceB;
    private String choiceC;
    private String choiceD;
    private int currentAnswer=-1;

    // 选词填空
    private List<String> optionalWord;//选词填空的15个选项

    public Question(String question, List<String> optionalWord) {
        this.question = question;
        this.optionalWord = optionalWord;
    }

    // 快速阅读
    public Question(String question) {
        this.question = question;
    }

    public Question(String question, String choiceA, String choiceB, String choiceC, String choiceD) {
        this.question = question;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
        this.choiceD = choiceD;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public String getChoiceD() {
        return choiceD;
    }

    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD;
    }

    public List<String> getOptionalWord() {
        return optionalWord;
    }

    public void setOptionalWord(List<String> optionalWord) {
        this.optionalWord = optionalWord;
    }

    public int getCurrentAnswer() {
        return currentAnswer;
    }

    public void setCurrentAnswer(int currentAnswer) {
        this.currentAnswer = currentAnswer;
    }
}
