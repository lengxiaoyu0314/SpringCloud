package com.springboot.cloud.auth.authentication.temptest;

import java.util.concurrent.TimeUnit;

/**
 * 指令重排序，以及线程可见性
 * Java模型，数据结构
 * 测试代码-server 变成死循环，没加就是正常（可见性问题）
 * 通过设置JVM的参数，打印出jit编译的内容（这里说的是非class文件）
 * -server -XX:+UnlockDiagnosticVmOptions -XX:+PrintAssembly -XX:+LogCompilation -XX:LogFile=jit.log
 * -server -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:+LogCompilation -XX:LogFile=jit.log
 */
public class VisiBiltyDemo {
    private volatile Boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        VisiBiltyDemo demo =  new VisiBiltyDemo();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i =0;
                //class -> 运行时jit编译 ->汇编指令 -> 重排序
                while(demo.flag){//指令重排序
                    i++;

                }
                System.out.println(i);
            }
        }
        ).start();
        TimeUnit.SECONDS.sleep(2000);
        //设置is为false,使上面的线程结束while循环
        demo.flag=false;
        System.out.println("demo.flag");
    }
}
