package com.example.beans;

/**
 * Created by: lzw.
 * Date: 2020/5/30
 * Time: 15:57
 * Description：
 * 1、账户类
 * 用于记录当前账户信息（id）
 */
public class Account {

    //使用 手机号 作为唯一id
    private String id;

    public Account(String telephone){
        this.id = telephone;
    }

    public String getId() {
        return id;
    }
}
