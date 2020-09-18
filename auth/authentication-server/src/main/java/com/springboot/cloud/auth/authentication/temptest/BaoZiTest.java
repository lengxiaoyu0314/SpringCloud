package com.springboot.cloud.auth.authentication.temptest;

import java.util.concurrent.locks.LockSupport;

public class BaoZiTest {


    //包子店
    public static Object baozidian = null;
    /**
     * suspend和resume死锁现象
     * 死锁现象有同步代码块、先后顺序都会造成死锁现象
     */
    public void suspendTest() throws Exception{
        //线程启动
        Thread consumerThread = new Thread(() ->{
            while (baozidian == null) {
                System.out.println("1,没包子，进入等待！");
                Thread.currentThread().suspend();

            }
            System.out.println("2,买到包子了，回家了");
        });
        consumerThread.start();
        //3秒后，生产一个包子
        Thread.sleep(3000L);
        baozidian = new Object();
        consumerThread.resume();
        System.out.println("3,通知消费者");
    }

    /**
     * wait/notify机制，这些方法只能由用一个对象锁持有者线程调用，也是卸载同步代码块里面，否则抛出异常
     * @param会释放锁
     * @throws Exception
     */
    public void waitNotifyTest() throws  Exception{
        new Thread(()->{
            if(baozidian == null) {//如果没有包子，进入等待
                synchronized (this){//同一个对象锁，包子店对象
                    try{
                        System.out.println("1进入等待");
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("2,买到包子，回家");
        }).start();
        //3秒之后，生产一个包子
        Thread.sleep(3000L);
        baozidian = new Object();
        synchronized (this){
            this.notify();
            System.out.println("3,通知消费者");

        }
    }

    /**
     * park/unpark不会释放锁
     * @throws Exception
     */
    public void parkUnparkDeadLockTest()throws  Exception{
        Thread cousumerThread = new Thread(()->{
            if(null == baozidian){
                System.out.println("1,进入等待");
                //当前线程拿到锁，然后挂起
                synchronized (this){
                    LockSupport.park();
                }
            }
            System.out.println("2,买到包子了，回家");
        });
        cousumerThread.start();
        //3秒后，生产一个包子
        Thread.sleep(3000L);
        baozidian = new Object();
        LockSupport.unpark(cousumerThread);
        //争取到锁后，在恢复
//        synchronized (this) {
//
//        }
        System.out.println("3,通知消费者");
    }
    public static void main(String[] args) throws Exception {
//        new BaoZiTest().suspendTest();
//        new BaoZiTest().waitNotifyTest();
        new BaoZiTest().parkUnparkDeadLockTest();
    }
}
