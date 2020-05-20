package com.suntr.juc;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreSample {
    public static void main(String[] args) {
        // 模拟厕所10个茅坑
        Semaphore semaphore = new Semaphore(5);
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                try {
                    // 获取锁资源
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "\t上厕所");
                    // 模拟人上厕所10秒，然后让出坑位
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println(Thread.currentThread().getName() + "\t上完厕所，让出坑位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 释放锁资源
                    semaphore.release();
                }
            }, "" + i + "号帅哥").start();
        }
    }
}
