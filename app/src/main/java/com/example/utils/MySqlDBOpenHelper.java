package com.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接帮助类
 */
public class MySqlDBOpenHelper {
    private static String diver = "com.mysql.jdbc.Driver";
    private static String dataBase = "english_exam_system_database";
    // 遇到的问题：https://blog.csdn.net/qq_36478274/article/details/105156418
    private static String ip = "10.0.2.2";
    //加入utf-8是为了后面往表中输入中文，表中不会出现乱码的情况
    private static String url = "jdbc:mysql://"+ip+":3307/"+dataBase+"?characterEncoding=utf-8";
    private static String user = "root";//用户名
    private static String password = "lzw893846649";//密码

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

}
