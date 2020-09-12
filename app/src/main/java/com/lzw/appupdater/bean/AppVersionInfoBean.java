package com.lzw.appupdater.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * @author Li Zongwei
 * @date 2020/9/9
 **/
public class AppVersionInfoBean implements Serializable {

    private String title;
    private String content;
    private String url;
    private String md5;
    private String versionCode;

    private AppVersionInfoBean(String title, String content, String url, String md5, String versionCode) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.md5 = md5;
        this.versionCode = versionCode;
    }

    /**
     * 把response转换为AppVersionInfoBean。
     *
     * @param response
     * @return
     */
    public static AppVersionInfoBean parse(String response) {
        try {
            JSONObject responseJson = new JSONObject(response);
            String title = responseJson.optString("title");
            String content = responseJson.optString("content");
            String url = responseJson.optString("url");
            String md5 = responseJson.optString("md5");
            String versionCode = responseJson.optString("versionCode");

            //TODO 是否需要对获取到的值进行检验
            // 不应该在这里检测，检测属于使用这个bean，不适合在这里处理

            return new AppVersionInfoBean(title,content,url,md5,versionCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public String getMd5() {
        return md5;
    }

    public String getVersionCode() {
        return versionCode;
    }
}
