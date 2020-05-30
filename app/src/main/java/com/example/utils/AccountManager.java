package com.example.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.beans.Account;

/**
 * Created by: lzw.
 * Date: 2020/5/30
 * Time: 15:53
 * Description：
 * 1、账户管理类
 * 用来判断当前账户是否登录
 * 登录后，可以把自己的
 *  单词本（生词本、已学单词本）；
 *  收藏的题目
 * 保存到数据库中
 */
public class AccountManager {
    private static AccountManager instance;
    private final SharedPreferences mSharedPreferences;

    private boolean isOnline;
    private String token;

    private String telephone;

    private AccountManager(Application application){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);

        isOnline = mSharedPreferences.getBoolean("isOnline",false);
        token = mSharedPreferences.getString("token",null);

        telephone = mSharedPreferences.getString("telephone",null);
    }

    public static AccountManager getInstance(Application application){
        if (instance == null){
            instance = new AccountManager(application);
        }
        return instance;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
        mSharedPreferences.edit().putBoolean("isOnline",isOnline).apply();
    }

    public void setToken(String token) {
        this.token = token;
        mSharedPreferences.edit().putString("token",token).apply();
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
        mSharedPreferences.edit().putString("telephone",telephone).apply();
    }

    public String getToken() {
        return token;
    }

    public String getTelephone() {
        return telephone;
    }

    public boolean isOnline() {
//        return isOnline && !TextUtils.isEmpty(token);
        return isOnline;
    }

    /**
     * 退出登录
     */
    public void logout(){
        setOnline(false);
        setToken(null);
        setTelephone(null);
    }
}
