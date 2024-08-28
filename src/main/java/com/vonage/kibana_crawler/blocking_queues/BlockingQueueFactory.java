package com.vonage.kibana_crawler.blocking_queues;

import com.vonage.kibana_crawler.configs.CrawlerEnvironment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public final class BlockingQueueFactory<T> {

    private final List<CrawlerBlockingQueue> QUEUE_POOL = new ArrayList<>();

    private final Object lockA = new Object();

    private final Lock lock = new ReentrantLock();

    @Autowired
    public BlockingQueueFactory(CrawlerEnvironment environment) {
        for (int i=0; i<environment.getBlockingQueuePoolSize(); i++){
            QUEUE_POOL.add(new CrawlerBlockingQueue(new LinkedBlockingQueue<>(), true));
        }
    }

    @Getter
    @AllArgsConstructor
    public class CrawlerBlockingQueue implements AutoCloseable{

        private final BlockingQueue<T> queue;

        @Setter
        private boolean available;

        @Override
        public void close() {
            synchronized (lockA){
                setAvailable(true);
                lockA.notify();
            }
        }
    }

    /* Currently a recursive function. A poor style. Use {@link java.util.concurrent.locks.ReentrantLock}. */
    public CrawlerBlockingQueue getBlockingQueue() throws InterruptedException {
        synchronized (lockA){
            for (CrawlerBlockingQueue queue : QUEUE_POOL) {
                if (queue.isAvailable()) {
                    queue.setAvailable(false);
                    return queue;
                }
            }
            log.info("Waiting for queue to be available");
            lockA.wait();
            log.info("Fetching queue again");
            return getBlockingQueue();
        }
    }
}
