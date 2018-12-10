package com.suntr.java9FlowSample.reactiveSample1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.LongStream;

/**
 * http://www.cnblogs.com/IcanFixIt/p/7245377.html
 */
public class FlowSample {

    public void publish() {

    }

    public static void main(String[] args) {
        CompletableFuture<Void> subscribeTask = null;

        try (SubmissionPublisher<Long> publisher = new SubmissionPublisher<>()) {
            System.out.println(publisher.getMaxBufferCapacity());
            subscribeTask = publisher.consume(System.out::println);
            LongStream.range(1L, 10L).forEach(publisher::submit);

        }
        if (subscribeTask!=null){
            try {
                subscribeTask.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
