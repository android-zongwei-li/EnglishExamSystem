package com.example.beans;

import androidx.annotation.Nullable;

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

    @Override
    public boolean equals(@Nullable Object obj) {

        if (this == obj){
            return true;
        }

        // 先判断一下obj是不是word的一个对象，不是，返回false
        if (!(obj instanceof Word)){
            return false;
        }

        // obj 向下转型，比较 word 属性是否相等
        Word word = (Word)obj;
        if (this.word.equals(word.word)){
            return true;
        }else {
            return false;
        }

    }
}
