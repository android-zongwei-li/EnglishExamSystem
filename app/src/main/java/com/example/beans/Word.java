package com.example.beans;

import java.io.Serializable;

public class Word implements Serializable {

    private String word;    //单词
    private String phonetic;//音标
    private String chinese; //中文意思

    public Word(String word, String chinese) {
        this.word = word;
        this.chinese = chinese;
    }

    public Word(String word, String phonetic, String chinese) {
        this.word = word;
        this.phonetic = phonetic;
        this.chinese = chinese;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }
}
