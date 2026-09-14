package com.example.shopping.infrastructure.messaging;

import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class InMemoryIssuanceQueue {

    private static final int CAPACITY = 10_000; // 큐의 크기
    private static final long POLL_TIMEOUT_MS = 100L; // 타임아웃
    private final LinkedBlockingQueue<IssuanceEvent> queue = new LinkedBlockingQueue<>(CAPACITY);

    public void enqueue(IssuanceEvent event) {
        if (!queue.offer(event)) {
            throw new BusinessException(ErrorCode.QUEUE_IS_FULL);
        }
    }
    public IssuanceEvent poll() throws InterruptedException{
        return queue.poll(POLL_TIMEOUT_MS, TimeUnit.MILLISECONDS);
    }
    public int size() {
        return queue.size();
    }
}
