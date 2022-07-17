package org.example.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

public class StampedLockSample {

    private static final StampedLock stampedLock = new StampedLock();
    private static final List<String> data = new ArrayList<>();

    public static void write(){
        long stamped = -1;
        try {
            stamped = stampedLock.writeLock();
            data.add("写入："+stamped);
            System.out.println("WRITE:" + stamped);
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            stampedLock.unlockWrite(stamped);
        }

    }

    public static void read(){
        long stamped = -1;
        try {
            stamped = stampedLock.readLock();
            for (String datum : data) {
                System.out.println("读取：" + datum);
            }
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            stampedLock.unlockRead(stamped);
        }
    }

    public static void optimisticRead(){
        long stamped = stampedLock.tryOptimisticRead();
        if (stampedLock.validate(stamped)) {
            try {
                stamped = stampedLock.readLock();
                for (String datum: data) {
                    System.out.println("乐观读：" + datum);
                }
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                stampedLock.unlockRead(stamped);
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++){
            executor.submit(()->{
                for (;;) {
                    read();
                }
            });
        }
        executor.submit(()->{
            for (;;){
                write();
            }
        });
        executor.shutdown();
    }

}
