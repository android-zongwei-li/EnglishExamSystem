package com.example.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by: lzw.
 * Date: 2020/5/25
 * Time: 9:49
 * Description：
 * 1、
 * 用于连接 服务器——eclipse+tomcat搭建的
 *
 * 这个是开始是为了获取听力音频，现在换了一种实现方式，此类暂时不用了
 */
public class ConnectServer {

    /**
     * 连接服务器
     */
    public static void getConn(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtils.i("连接服务器","正在连接");
                Socket socket = null;
                try {
                    //创建一个流套接字并将其连接到指定主机上的指定端口号
                    socket = new Socket("192.168.43.68",8888);

                    //读取服务器端数据
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    //向服务端发送数据
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    String str = "I an client";
                    out.writeUTF(str);

                    String ret = input.readUTF();
                    LogUtils.i("服务端返回的数据",ret);

                    out.close();
                    input.close();

                } catch (IOException e) {
                    LogUtils.e("客户端异常：",e.getMessage());
                }finally {
                    if (socket != null){
                        try {
                            socket.close();
                        } catch (IOException e) {
                            socket = null;
                            LogUtils.e("客户端finally异常",e.getMessage());
                        }
                    }
                }
            }
        }).start();
    }
}
