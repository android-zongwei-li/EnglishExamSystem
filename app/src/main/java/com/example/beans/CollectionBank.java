package com.example.beans;

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

    private List<CollectedListening> collectedListenings = new ArrayList<>();
    private List<CollectedReading> collectedReading = new ArrayList<>();
    private List<Translation> collectedTranslations = new ArrayList<>();
    private List<Writing> collectedWritings = new ArrayList<>();

    public List<CollectedListening> getCollectedListenings() {
        return collectedListenings;
    }

    public List<CollectedReading> getCollectedReading() {
        return collectedReading;
    }

    public List<Translation> getCollectedTranslations() {
        return collectedTranslations;
    }

    public List<Writing> getCollectedWritings() {
        return collectedWritings;
    }

    // 下列方法用于添加收藏
    public void add(CollectedListening listening){
        collectedListenings.add(listening);
    }
    public void add(CollectedReading reading){
        collectedReading.add(reading);
    }
    public void add(Translation translation){
        collectedTranslations.add(translation);
    }
    public void add(Writing writing){
        collectedWritings.add(writing);
    }

    // 下列方法用于移除收藏
    public void remove(CollectedListening listening){
        collectedListenings.remove(listening);
    }
    public void remove(CollectedReading reading){
        collectedReading.remove(reading);
    }
    public void remove(Translation translation){
        collectedTranslations.remove(translation);
    }
    public void remove(Writing writing){
        collectedWritings.remove(writing);
    }
}
