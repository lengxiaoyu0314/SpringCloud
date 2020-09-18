package com.springboot.cloud.auth.authentication.temptest;

/**
 * 官方提供的示例，检查有没有wordTearing情况
 */
public class WordTearing extends Thread{
    static final int LENGTH = 8;
    static final int ITERS = 8;
    static Thread[] threads = new Thread[LENGTH];
    static byte[] counts = new byte[LENGTH];

    final  int id;

    WordTearing(int i){
        id =i;
    }

    @Override
    public void run() {
        byte v = 0;
        for (int i = 0; i < ITERS; i++) {
            byte v2 = counts[id];
            if(v!=v2){
                System.err.println("Word-Tearing found:"+
                        "counts["+id+"]="+v2+",should be "+v);
                return;
            }
            v++;
            counts[id] =v;
        }
    }

}
