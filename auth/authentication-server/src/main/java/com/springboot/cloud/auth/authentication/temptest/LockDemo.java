package com.springboot.cloud.auth.authentication.temptest;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class LockDemo {

    volatile int i=0;
    static Unsafe unsafe;//直接操作内存，修改对象。。数组内存。。强大的Api
    private static  long valueOffset ;
    static {
        try {
            //反射技术获取unsafe
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);

            //获取i属性偏移量（用于定于value属性在内存中的具体地址）
            valueOffset = unsafe.objectFieldOffset(LockDemo.class.
                    getDeclaredField("i"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void add(){
//        i++;//三个步骤，并非原子操作
        //CAS+循环重试
        int current;
        do{
            //操作耗时的话，那么线程就会占用大量的Cpu执行时间
            current = unsafe.getIntVolatile(this,valueOffset);//读取
        }while(!unsafe.compareAndSwapInt(this,valueOffset,current,current+1));
        //有点类似版本号,乐观锁
        //可能回失败
    }

    public static void main(String[] args) throws InterruptedException {
        LockDemo Id = new LockDemo();

        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                for (int j = 0; j < 10000; j++) {

                    Id.add();
                }
            }).start();
        }
        Thread.sleep(2000L);
        System.out.println(Id.i);
    }
}
