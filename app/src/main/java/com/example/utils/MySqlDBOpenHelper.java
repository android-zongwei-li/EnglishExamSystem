package com.example.utils;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * 数据库连接帮助类
 */
public class MySqlDBOpenHelper {
    private static String diver = "com.mysql.jdbc.Driver";
    private static String dataBase = "english_exam_system_database";
    // 遇到的问题：https://blog.csdn.net/qq_36478274/article/details/105156418
 //   private static String ip = "10.0.2.2";
    private static String ip = "192.168.43.68";
 //   private static String ip = "localhost";

    //加入utf-8是为了后面往表中输入中文，表中不会出现乱码的情况
    private static String url = "jdbc:mysql://"+ip+":3306/"+dataBase+"?characterEncoding=utf-8&autoReconnect=true&useSSL=false";
 //   private static String url = "jdbc:mysql://"+ip+":3306/"+dataBase+"?useOldAliasMetadataBehavior=true";
    private static String user = "user";//用户名
    private static String password = "1234";//密码

    /*
     * 连接数据库
     * */
    public static Connection getConn(){
        Connection conn = null;
        try {
            Class.forName(diver);
            conn = DriverManager.getConnection(url,user,password);//获取连接
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 当前方法在登录后调用
     * 连接数据库
     * 根据用户手机号，获取用户表
     * 获取所有字段：主要是learnedWordsBook(已学单词本)、unfamiliarWordsBook(生词本)
     */
    public static HashMap<String,Object> getUserInfoFormMySql(final String phoneNums) {

        HashMap<String, Object> map = new HashMap<>();

        Connection conn = MySqlDBOpenHelper.getConn();

        String sql = "select * from user where telephone='" + phoneNums + "'";

        try {
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement(sql);

                if (ps != null) {
                    ResultSet rs = ps.executeQuery();

                    if (rs != null) {
                        int count = rs.getMetaData().getColumnCount();
                        LogUtils.i("USER", "总列数" + count);
                        while (rs.next()) {
                            for (int i = 1; i <= count; i++) {
                                String field = rs.getMetaData().getColumnName(i);
                                map.put(field, rs.getString(field));
                            }
                        }
                        rs.close();
                        ps.close();
                        conn.close();
                        return map;
                    }else {
                        return null;
                    }
                }else {
                    return null;
                }
            }else {
                Log.e("数据库异常","连接失败" );
                return null;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            Log.e("数据库异常","异常：" + e.getMessage());
            return null;
        }
    }

}
