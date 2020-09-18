package com.springboot.cloud.auth.authentication.temptest;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Java8New {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("A","C11","B11","E11","D");
        Predicate<String> list1 = (n) -> n.startsWith("D");
        Predicate<String> list2 = (n) -> n.length()==3;
        list.stream().filter(list2).forEach(n -> System.out.println(n));
//        list.forEach(n -> System.out.println(n));
//        list.forEach(System.out::println);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("new Thread() to do something!"+Thread.currentThread().getName());
//            }
//        }){
//            @Override
//            public synchronized void start() {
//                System.out.println("main线程优先处理完之后，再执行创建线程的run()方法!"+Thread.currentThread().getName());
//                super.start();
//            }
//        }.start();
//        new Thread(() ->
//            System.out.println("吃嘛嘛不行"+Thread.currentThread().getName())
//        ){
//            @Override
//            public synchronized void start() {
//                System.out.println("干活第一名"+Thread.currentThread().getName());
//                super.start();
//            }
//        }.start();
    }
}
