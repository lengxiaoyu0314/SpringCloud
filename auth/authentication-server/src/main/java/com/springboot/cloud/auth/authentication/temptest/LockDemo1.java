package com.springboot.cloud.auth.authentication.temptest;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

public class LockDemo1 {
//    volatile int value = 0;
    AtomicInteger  value = new AtomicInteger(0);

//    static Unsafe unsafe; // ������������������������������������������������....���������API
//    private static long valueOffset;
//
//    static {
//        try {
//            // ��������?���������unsafe��?
//            Field field = Unsafe.class.getDeclaredField("theUnsafe");
//            field.setAccessible(true);
//            unsafe = (Unsafe) field.get(null);
//
//            // ��������? value ����?�������������������������value����?������������������������������?
//            valueOffset = unsafe.objectFieldOffset(LockDemo1.class
//                    .getDeclaredField("value"));
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    public void add() {
        value.incrementAndGet();
        // TODO xx00
        // i++;// JAVA ������������������
        // CAS + ������ ������
//        int current;
//        do {
//            // ��������������������? ������ ���������������������������CPU������������
//            current = unsafe.getIntVolatile(this, valueOffset);
//        } while (!unsafe.compareAndSwapInt(this, valueOffset, current, current + 1));
        // ��������������?
    }

    public static void main(String[] args) throws InterruptedException {
        LockDemo1 ld = new LockDemo1();

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    ld.add();
                }
            }).start();
        }
        Thread.sleep(2000L);
        System.out.println(ld.value);
    }
}
