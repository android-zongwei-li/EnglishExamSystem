package com.example.beans;

import java.io.Serializable;

public class Writing implements Serializable {

    private String QuestionID;
    private String QuestionType;    //题型
    private String Subject; //科目
    private String Year;    //年份
    private String Month;   //月份
    private String Index;   //第几套题目
    private String Question;//作文题目
    private String Answer;  //范文

    // 目前 QuestionType、Subject 暂时用不到
    public Writing(String questionID, String year, String month, String index, String question, String answer) {
        QuestionID = questionID;
        Year = year;
        Month = month;
        Index = index;
        Question = question;
        Answer = answer;
    }

    public String getQuestionID() {
        return QuestionID;
    }

    public void setQuestionID(String questionID) {
        QuestionID = questionID;
    }

    public String getQuestionType() {
        return QuestionType;
    }

    public void setQuestionType(String questionType) {
        QuestionType = questionType;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getIndex() {
        return Index;
    }

    public void setIndex(String index) {
        Index = index;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }
}
