package com.example.shopping.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IssuanceEventHandler { // v4: 스프링 큐: 워커와 동일한 기능 수행

    private final IssuanceWriter issuanceWriter;

    @Async(AsyncIssuanceConfig.ISSUANCE_TASK_EXECUTOR) // 백그라운드에서 수행
    @EventListener // 스프링이 제공하는 이벤트 리스너
    public void handle(IssuanceEvent event) {
        issuanceWriter.write(event);
    }
}
