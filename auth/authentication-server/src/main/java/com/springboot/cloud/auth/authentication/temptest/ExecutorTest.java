package com.springboot.cloud.auth.authentication.temptest;

import java.util.List;
import java.util.concurrent.*;

/**
 * 线程池的概念
 * Executor最上层的接口，定义了执行任务的方法execute
 * ExecutorService ,继承了Executor接口，拓展了Callable、Future、关闭方法
 * ScheduledExecutorService 继承了ExecutorService,增加了定时任务相关的方法
 * ThreadPoolExecutor 基础、标准的线程池实现
 * ScheduledThreadPoolExecutor 继承了ThreadPoolExecutor,实现了SceduleExecutorSerivce中相关定时人物的方法
 *
 */
public class ExecutorTest {

    public void testCommon(ThreadPoolExecutor threadPoolExecutor) throws Exception{
        //测试，提交15个执行时间需要3秒的任务，看超过大小的2个，对应处理情况
        for (int i = 0; i < 15; i++) {
            int n = i;
            threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                  try {
                      System.out.println("开始执行任务："+n);
                      Thread.sleep(3000L);
                      System.out.println("结束执行任务："+n);
                  }catch (InterruptedException e){
                      e.printStackTrace();
                  }
                }
            });
            System.out.println("任务提交成功："+i);
        }
        //查看线程数量，查看队列等待数量
        Thread.sleep(500L);
        System.out.println("当前线程池数量为："+threadPoolExecutor.getPoolSize());
        System.out.println("当前线程池等待的数量为："+threadPoolExecutor.getQueue().size());
        //等待15秒，查看线程数量和队列数量（理论上，会超出核心线程数量会自动销毁）
        Thread.sleep(15000L);
        System.out.println("当前线程池数量为："+threadPoolExecutor.getPoolSize());
        System.out.println("当前线程池等待的数量为："+threadPoolExecutor.getQueue().size());    }
    /**
     * 线程池信息，核心线程数量5，最大数量10，无界队列超出狠心线程数量的线程存活时间5秒，指定拒绝策略的
     * @throws Exception
     * 达到核心线程数量，就会去判断队列是否已经满了，如果满了，再创建到最大线程数量
     */
    private void threadPoolExecutorTest1() throws Exception{
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,10,5, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
        testCommon(threadPoolExecutor);
        //预计结果：线程池线程数量为：5，超出数量的任务，其他的进入队列中等待被执行

    }

    /**
     * 跟上面的线程池相比，本次有界队列，队列为3，最大线程数量是10，也就是说最大容纳13个任务
     * @param
     * @throws Exception
     */
    private void threadPoolExecutorTest2() throws Exception{
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(3)
                , new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.err.println("任务被拒绝了");
            }
        });
        testCommon(threadPoolExecutor);
    }

    /**
     * 无界队列，最大线程数等于核心线程数量
     * @throws Exception
     */
    private void threadPoolExecutorTest3() throws Exception{
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,5,5, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
        testCommon(threadPoolExecutor);
        //预计结果：线程池线程数量为：5，超出数量的任务，其他的进入队列中等待被执行

    }
    /**
     * 无界队列，最大线程数integer的最大范围，核心线程数量为0，线程存活60S
     * 队列用的是SynchronousQueue同步队列，其实不是一个真正的队列，因为它不会为队列中的元素维护存储空间。
     * 与其他队列不同，它维护的是一组线程，这些线程在等待元素的移入和移除
     * 客户端代码向线程池提交任务时，而线程池中又没有空闲的线程能狗从SynchronousQueue同步队列
     * 中取出任务时，那么相应的offer方法调用就会失败（即任务没有存入队列）
     * 此时，ThreadPoolExecutor会创建一个新的工作者线程用于对这个入队列失败的任务进行处理
     * @throws Exception
     */
    private void threadPoolExecutorTest4() throws Exception{
        //同下Executors.newCachedThreadPool()一样的
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0,Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
        testCommon(threadPoolExecutor);
        //预计结果：线程数量随着任务的增加，有限队列的限制，线程数量疯狂增长
        //当所有任务执行完毕后，等待60s线程数量为0了
        Thread.sleep(6000L);
        System.out.println("线程数量为："+threadPoolExecutor.getPoolSize());
    }

    /**
     * 定时执行线程池信息；3秒后执行，一次性任务，到点就执行
     * 核心线程信息数量为5，最大线程数量为Integer.MAX_VALUE DelayedWordQueue延时队列，超出核心线程数量的线程存活时间：0秒
     * @throws Exception
     */
    private void threadPoolExecutorTest5() throws Exception{
        //和Executors.newScheduledThreadPool()一样的
        ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(5);
        threadPoolExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("开始执行任务，时间为："+System.currentTimeMillis());
            }
        },3000,TimeUnit.MILLISECONDS);
        System.out.println("定时任务，提交成功，时间是："+System.currentTimeMillis()+",当前线程池中线程数量为："
        +threadPoolExecutor.getPoolSize());
        //预计结果：任务在3秒后被执行一次
    }

    /**
     * 定时执行线程信息，线程固定数量为5
     * @param
     * @throws Exception
     */
    private void threadPoolExecutorTest6() throws Exception{
        //和Executors.newScheduledThreadPool()一样的
        ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(5);
        //周期性的执行某一个任务，线程池提供了两种调度方式，这里单独演示一下，测试场景一样
        //测试场景，提价的任务需要3秒才能执行完毕，看两种不哦那个调度的区别
        //效果1，提交后，2秒后开始第一次执行，之后每隔1秒，固定执行一次（如果发现上次执行还未完毕，则等待完毕，完毕后立刻执行）
        //也就是说这个代码中是，3秒钟执行一次（计算方式，每次执行三秒，间隔时间1秒，执行结束后马上开始下一次执行，无需等待）
        /*threadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("任务1开始执行，时间为："+System.currentTimeMillis());
            }
        },2000,1000,TimeUnit.MILLISECONDS);*/
        //效果2：提交后，2秒后开始第一次执行，之后每隔1秒，固定执行一次（如果发现上次执行还未完毕，则等待完毕之后）
        //也就是说这个代码中的效果看到的是，4秒执行一次，（计算公式：每次执行3秒，间隔时间1秒，执行完以后需要等待1秒，所以是3+1）
        threadPoolExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("任务2开始执行，时间为："+System.currentTimeMillis());

            }
        },2000,1000,TimeUnit.MILLISECONDS);
    }

    /**
     * 最大线程为10，核心线程数量为5，队列长度为3
     * @throws Exception
     */
    private void threadPoolExecutorTest7() throws Exception{
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(3)
                , new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.err.println("任务被拒绝了");
            }
        });
        testCommon(threadPoolExecutor);
        //1秒后终止线程
        Thread.sleep(1000L);
        //立刻关闭线程
        threadPoolExecutor.shutdown();
        //继续向关闭的线程池提交任务
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                System.err.println("追加了一个任务");
            }
        });
    }
    /**立刻终止线程池
     * 最大线程为10，核心线程数量为5，队列长度为3
     * @throws Exception
     */
    private void threadPoolExecutorTest8() throws Exception{
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(3)
                , new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.err.println("任务被拒绝了");
            }
        });
        testCommon(threadPoolExecutor);
        //1秒后终止线程
        Thread.sleep(1000L);
        //立刻关闭线程
        List<Runnable> runnableList = threadPoolExecutor.shutdownNow();
        //继续向关闭的线程池提交任务
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                System.err.println("追加了一个任务");
            }
        });
        System.err.println("未结束的任务有"+runnableList.size());
    }

    /**
     * 密集型运算 线程数为cpu*2，网络io运算cpu多背
     * 如tomcat默认线程数为200
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new ExecutorTest().threadPoolExecutorTest8();
    }


}
