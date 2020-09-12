package com.lzw.activity.activity_in_fragment3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.lzw.activity.base.BaseAppCompatActivity;
import com.lzw.englishExamSystem.R;
import com.lzw.utils.AccountManager;
import com.lzw.utils.database.MySqlDBOpenHelper;
import com.lzw.utils.PhoneNumberUtils;
import com.lzw.view.topbar.TopBar;
import com.mob.MobSDK;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class LoginActivity extends BaseAppCompatActivity {

    public static final String PARAM_PHONE = "phoneNumber";//电话号码，用于给上个界面返回数据

    // MobTech 手机验证码服务
    // http://www.mob.com/
    String APPKEY = "2e18012850b17";
    String APPSECRETE = "9e17413893d32176b7e88f7ce67ea54f";

    TopBar topBar;
    // 手机号输入框
    private EditText inputPhoneEt;
    // 验证码输入框
    private EditText inputCodeEt;
    // 获取验证码按钮
    private Button requestCodeBtn;
    // 登录按钮
    private Button loginBtn;

    //倒计时
    int i = 30;

    // 输入的手机号
    String phoneNums;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 111){
                Toast.makeText(getApplicationContext(), "连接数据库失败",
                        Toast.LENGTH_SHORT).show();
            }
            if (msg.what == -9) {
                requestCodeBtn.setText( i + " s");
            }
            if (msg.what == -8) {
                requestCodeBtn.setText("获取验证码");
                requestCodeBtn.setClickable(true);
                i = 30;
            }
            else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);

                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    if (result == SMSSDK.RESULT_COMPLETE){
                        Toast.makeText(getApplicationContext(), "已发送",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                    // 提交验证成功
                    // 请求数据库，判断当前手机号是否存在，不存在则创建
                    // 关闭登录页面，跳转到app中，更新 Fragment3 中的数据
                    if (result == SMSSDK.RESULT_COMPLETE){

                        // 连接数据库进行操作需要在主线程操作
                        queryPhoneNumber(phoneNums);    // 把输入的手机号传进去
                        Intent intent = new Intent();
                        intent.putExtra(PARAM_PHONE,phoneNums);
                        setResult(RESULT_OK,intent);

                        saveAccount(phoneNums,true);

                        finish();
                    }else { // 验证失败，toast提示
                        Toast.makeText(getApplicationContext(), "验证码输入有误",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        // 启动短信验证sdk
        MobSDK.init(this, APPKEY, APPSECRETE);
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                // 获取到事件后，把具体的处理交给handle
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);

    }

    /**
     * 控件初始化
     */
    private void initView() {

        topBar = findViewById(R.id.topBar);
        inputPhoneEt = findViewById(R.id.et_login_input_phone);
        requestCodeBtn = findViewById(R.id.btn_login_request_code);
        inputCodeEt = findViewById(R.id.et_login_input_code);
        loginBtn = findViewById(R.id.btn_login);

        // topBar
        topBar.setTitle("登录");
        topBar.setRighttIsVisable(false);
        topBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });

        // 获取验证码
        requestCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //
                phoneNums = inputPhoneEt.getText().toString();
                // 1. 通过规则判断手机号
                if (!PhoneNumberUtils.judgePhoneNums(phoneNums)) {
                    Toast.makeText(getBaseContext(), "手机号码输入有误！", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 2. 通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phoneNums);

                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                requestCodeBtn.setClickable(false);
                requestCodeBtn.setText( i + " s");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);// 刷新UI
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createProgressBar();

                if (inputPhoneEt.getText().toString().length() == 0){
                    Toast.makeText(getApplicationContext(), "请输入手机号",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!PhoneNumberUtils.judgePhoneNums(inputPhoneEt.getText().toString())){
                    Toast.makeText(getApplicationContext(), "请输入正确的手机号",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (inputCodeEt.getText().toString().length() == 0){
                    Toast.makeText(getApplicationContext(), "请输入验证码",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // 将收到的验证码和手机号提交再次核对
                SMSSDK.submitVerificationCode("86", phoneNums, inputCodeEt
                        .getText().toString());

            }
        });

    }

    private void createProgressBar() {
        FrameLayout layout = findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        ProgressBar mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }

    /**
     * 查询数据库：当前手机号是否存在
     * 存在：查询结束，不作处理
     * 不存在：插入数据
     * @param phoneNums
     */
    private void queryPhoneNumber(final String phoneNums){
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                conn = MySqlDBOpenHelper.getConn();
                String sql = "select telephone from user where telephone='"+phoneNums+"'";
                Statement st;
                //向user表插入数据，其中telephone字段插入的值对应第一个问号
                String sql_insert = "insert into user (telephone) values (?);";
                PreparedStatement pstm = null;
                try {
                    st = conn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    rs.last();//指向最后
                    if (rs.getRow() == 0){  // 如果行号为0，表示里面没有数据
                        // 没数据的话，就把号码存入
                        pstm = conn.prepareStatement(sql_insert);
                        //通过setString给第一个个问号赋值
                        pstm.setString(1, phoneNums);
                        pstm.executeUpdate();
                    }
                    if (pstm != null){
                        pstm.close();
                    }
                    rs.close();
                    st.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 把号码插入数据库
     * @param phoneNums
     */
    private void insetPhoneNum(final String phoneNums) {

    }

    /**
     * 把登录的账号数据保存下来，后面使用个人单词本和收藏需要用到账户
     * @param telephone 账户唯一标识（id）
     * @param isOnline  //暂时用不到
     */
    private void saveAccount(String telephone,Boolean isOnline){
        //把当前的账户信息保存下来
        AccountManager am = AccountManager.getInstance(getApplication());
        am.setTelephone(telephone);
        am.setOnline(isOnline);
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }

}
