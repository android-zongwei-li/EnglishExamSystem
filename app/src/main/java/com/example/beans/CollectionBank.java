package com.example.beans;

import com.example.utils.testPaperUtils.TestPaperFromWord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: lzw.
 * Date: 2020/4/29
 * Time: 11:28
 * Description：
 * 1、用来存储收藏的题目
 * 2、提供添加个收藏的方法，并提供获取当前收藏项的方法
 */
public class CollectionBank {

    private static CollectionBank mCollectionBank = new CollectionBank();
    private CollectionBank(){

    }
    public static CollectionBank getInstance(){
        return mCollectionBank;
    }

    private List<TestPaperFromWord.ListeningFormWord> collectedListenings = new ArrayList<>();
    private List<TestPaperFromWord.ReadingFormWord> collectedReadings = new ArrayList<>();
    private List<Translation> collectedTranslations = new ArrayList<>();
    private List<Writing> collectedWritings = new ArrayList<>();

    public List<TestPaperFromWord.ListeningFormWord> getCollectedListenings() {
        return collectedListenings;
    }

    public List<TestPaperFromWord.ReadingFormWord> getCollectedReadings() {
        return collectedReadings;
    }

    public List<Translation> getCollectedTranslations() {
        return collectedTranslations;
    }

    public List<Writing> getCollectedWritings() {
        return collectedWritings;
    }

    // 下列方法用于添加收藏
    public void add(TestPaperFromWord.ListeningFormWord listening){
        collectedListenings.add(listening);
    }
    public void add(TestPaperFromWord.ReadingFormWord reading){
        collectedReadings.add(reading);
    }
    public void add(Translation translation){
        collectedTranslations.add(translation);
    }
    public void add(Writing writing){
        collectedWritings.add(writing);
    }

    // 下列方法用于添加收藏
    public void remove(TestPaperFromWord.ListeningFormWord listening){
        collectedListenings.remove(listening);
    }
    public void remove(TestPaperFromWord.ReadingFormWord reading){
        collectedReadings.remove(reading);
    }
    public void remove(Translation translation){
        collectedTranslations.remove(translation);
    }
    public void remove(Writing writing){
        collectedWritings.remove(writing);
    }
}
