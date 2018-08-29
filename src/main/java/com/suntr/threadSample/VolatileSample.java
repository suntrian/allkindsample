package com.suntr.threadSample;

/**
 *
 */

import java.util.concurrent.atomic.AtomicInteger;

public class VolatileSample {

    public volatile static int race = 0;
    public static AtomicInteger atomicRace = new AtomicInteger(0);
    public volatile static Integer syncRace = 0;
    public static Object lock = new Object();
    private static final int THREADS_COUNT = 20;

    public static void increace() {
        race++;
    }

    public static void atomicIncreace() {
        atomicRace.incrementAndGet();
    }

    public static void testIncreace() {
        Thread[] threads = new Thread[THREADS_COUNT];
        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        increace();
                    }
                }
            });
            threads[i].start();
        }
        while (Thread.activeCount() > 1) {
            Thread.yield();
        }
        System.out.println(race);
    }

    public static void testSyncIncrease() {
        Thread[] threads = new Thread[THREADS_COUNT];
        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        synchronized (lock) {
                            syncRace ++;
                        }
                    }
                }
            });
            threads[i].start();
        }
        while (Thread.activeCount() > 1) {
            Thread.yield();
        }
        System.out.println(syncRace);
    }

    public static void testAtomicIncrease() {
        Thread[] threads = new Thread[THREADS_COUNT];
        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        atomicIncreace();
                    }
                }
            });
            threads[i].start();
        }
        while (Thread.activeCount() > 1) {
            Thread.yield();
        }
        System.out.println(atomicRace);
    }


    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        testIncreace();
        System.out.println(System.currentTimeMillis());
        testAtomicIncrease();
        System.out.println(System.currentTimeMillis());
        testSyncIncrease();
        System.out.println(System.currentTimeMillis());
    }
}
