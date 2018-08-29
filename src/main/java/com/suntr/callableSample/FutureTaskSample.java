package com.suntr.callableSample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskSample {

    public static void main(String[] args) {
        Callable<int[]> primeCallable = new PrimeCallable(1000);
        FutureTask<int[]> primeTask = new FutureTask<>(primeCallable);
        Thread t = new Thread(primeTask);
        t.start();
        try {
            // Thread.sleep(5000);
            while (!primeTask.isDone()) {
                Thread.sleep(2);
            }

            if (primeTask.isDone()) {
                int[] primes = primeTask.get();
                for (int i: primes) {
                    System.out.println(i);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


}

class PrimeCallable implements Callable<int[]> {

    private int max;

    public PrimeCallable(int max) {
        this.max = max;
    }

    @Override
    public int[] call() throws Exception {
        int[] prime = new int[max+1];
        List<Integer> list = new ArrayList<>();
        for (int i = 2; i <= max; i++) {
            prime[i] = 1;
        }
        for (int i = 2; i*i <= max; i++) {
            if (prime[i] == 1) {
                for (int j = 2*i; j <= max; j++) {
                    if (j % i == 0) {
                        prime[j] = 0;
                    }
                }
            }
        }
        for (int i = 2; i < max; i++) {
            if (prime[i] == 1) {
                list.add(i);
            }
        }
        int[] p = new int[list.size()];
        for (int i = 0; i < p.length; i++) {
            p[i] = list.get(i).intValue();
        }
        return p;
    }
}