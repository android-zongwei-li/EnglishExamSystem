package com.lzw.utils;

import android.content.Context;

import androidx.annotation.RawRes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 处理有关 txt文件 的工具类
 */
public class TxtUtils {

    private Context context;

    private InputStream is = null;
    private BufferedReader br = null;
    private InputStreamReader isr = null;

    public TxtUtils(Context context){
        this.context = context;
    }

    /**
     *   从raw中读取txt文件
     *
     * @param rawResId
     * @return
     */
    public String readTxtFileFromRaw(@RawRes int rawResId){

        // 从txt中读取到的字符串
        String readString = "";

        try {
            is = context.getResources().openRawResource(rawResId);//文件输入流
            isr = new InputStreamReader(is,"gb2312");//包装为字符流
            br = new BufferedReader(isr);//包装为缓冲流
            String line = null;
            while ((line = br.readLine()) != null ){
                readString += line;
            }
            return readString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            if (br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null){
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 把txt文件中的数据转换为json对象
     * @param rawResId
     */
    public JSONObject rawTxtToJson(@RawRes int rawResId){
        // 从txt文件中读取到的string
        String readString = readTxtFileFromRaw(rawResId);

        // string to json
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(readString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}
