package com.example.shopping.infrastructure.messaging;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryIssuanceWorker { // 메시지를 꺼내는 worker 스레드
    private final InMemoryIssuanceQueue queue;
    private final IssuanceWriter issuanceWriter;
    private Thread workerThread;

    @PostConstruct //빈이 로딩되자마자 실행
    public void start() {
        workerThread = new Thread(this::runLoop, "issuance-worker");
        workerThread.setDaemon(true); // 데몬 스레드로 지정
    }
    private void runLoop() {
        while(!Thread.currentThread().isInterrupted()) {
            IssuanceEvent event;
            try {
                event =  queue.poll(); // 메시지를 꺼냄

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            if (event == null) { //메시지가 null이면 계속 꺼냄
                continue;
            }
            try {
                // DB 로직 처리
                issuanceWriter.write(event);
            } catch (Exception e) {
                //
                log.error("Worker write 실패: couponId = {}, userId = {}",
                        event.getCouponId(),
                        event.getUserId(),
                        e
                );
            }
        }
        log.info("issuance-worker 종료");
    }

    @PreDestroy // 빈이 삭제되었을 때 실행(훅)
    public void stop() {
        if (workerThread != null) {
            workerThread.interrupt();
        }
    }

}
