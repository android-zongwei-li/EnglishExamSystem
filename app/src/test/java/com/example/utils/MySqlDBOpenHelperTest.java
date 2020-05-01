package com.example.utils;

import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlDBOpenHelperTest {

    @Test
    public void getConn() {
        final String telephone = "16651605583";
        Connection conn = MySqlDBOpenHelper.getConn();
        System.out.println(conn);
        String sql = "select telephone from user where telephone='"+telephone+"'";
        Statement st;
        try {
            st = conn.createStatement();
            String sql_insert = "insert into user (telephone) values (?);";
            PreparedStatement pstm = conn.prepareStatement(sql_insert);
            //通过setString给第一个个问号赋值
            pstm.setString(1, telephone);
            pstm.executeUpdate();
            ResultSet rs = st.executeQuery(sql);
            rs.last();
            if (rs.getRow() == 0){
                System.out.println("wu");
            }
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}