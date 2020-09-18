package com.springboot.cloud.auth.authentication.temptest;

/**
 * 线程共享的方式有三种
 * 网络共享
 * 文件共享
 * 变量共享
 *
 */
public class ThreadTest {
    //共享变量
    public static String content ="null";

    public static void main(String[] args) {
        //线程1写入数据
        new Thread(()-> {

        }).start();
    }
}

