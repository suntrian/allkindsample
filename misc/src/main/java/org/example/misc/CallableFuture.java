package org.example.misc;

import java.util.concurrent.*;

public class CallableFuture {

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        // Future是一个接口，该接口用来返回异步的结果。
        Future<String> future = executorService.submit(new TaskClassable());

        System.out.println(future.get(10000, TimeUnit.MILLISECONDS));
        System.out.println("Finished");
    }

    static class TaskClassable implements Callable<String> {

        @Override
        public String call() throws Exception {
            Thread.sleep(5000);
            return "callstatus=OK";
        }
    }
}
