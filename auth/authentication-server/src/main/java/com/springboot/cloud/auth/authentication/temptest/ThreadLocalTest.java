package com.springboot.cloud.auth.authentication.temptest;

/**
 * 线程封闭和栈封闭的概念
 */
public class ThreadLocalTest {

    //设置ThreaLocal变量，理解为是一个Map,map的存储形式为<ThreadId,ThreadValue>,以每个线程的id为key,值为每个线程自己设置的值
    public static ThreadLocal<String> values = new ThreadLocal<>();

    public static void main(String[] args) throws Exception {

        //主线程操作
        values.set("123");
        String v = values.get();
        System.out.println("这是主线程操作的值"+v);
        new Thread(()->{
            System.out.println("副线程操作之前获得的"+values.get());
            values.set("456");
            System.out.println("副线程操作之后的"+values.get());
        }).start();
        //主线程，等待副线程完成设置
        Thread.sleep(5000L);
        System.out.println("再次查看主线程操作的值"+values.get());

    }


}
