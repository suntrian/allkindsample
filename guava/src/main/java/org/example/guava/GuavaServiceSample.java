package org.example.guava;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class GuavaServiceSample extends AbstractExecutionThreadService {

    private LinkedBlockingQueue<String> taskIdQueue = new LinkedBlockingQueue<>();

    @Override
    protected void run() throws Exception {
        for (int i = 0; i < 10; i++) {
            String task = taskIdQueue.take();
            log.info(task);
        }
    }

    public static void main(String[] args) throws Exception {
        GuavaServiceSample sample = new GuavaServiceSample();
        sample.startAsync();
        for (int i =0; i < 100; i++) {
            sample.taskIdQueue.put(RandomStringUtils.random(6));
        }
    }

}
