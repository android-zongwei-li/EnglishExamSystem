package com.example.utils.testPaperUtils;

import android.content.Context;

import androidx.annotation.RawRes;

import org.textmining.text.extraction.WordExtractor;

import java.io.InputStream;

/**
 * 读取word文件
 * 现在写这个类的目的是：
 * 想把四级真题(word)取出来
 * 实现逻辑：
 * 1、首先是把word资源放到raw中，然后从raw中获取word，并转化为string
 * @see WordUtils#readWord(int)
 * 2、
 */
public class WordUtils {

    private Context context;

    WordUtils(Context context){
        this.context = context;
    }

    /**
     * 把doc中的内容，转化为string
     * @param rawResId  raw中的资源文件id
     * @return  word转换得到的string
     */
    String readWord(@RawRes int rawResId){
        //创建输入流用来读取doc文件
        String text = null;
        try {
            InputStream is = context.getResources().openRawResource(rawResId);//文件输入流
            //创建WordExtractor
            WordExtractor extractor = new WordExtractor();
            //进行提取对doc文件
            text = extractor.extractText(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

}
