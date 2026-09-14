package com.example.shopping.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
class IssuanceWriter {
    // @Transactional은 public 메서드에서만 활성화되기 때문에 분리
    private final IssuanceTransactionalWriter issuanceTransactionalWriter;

    public void write(IssuanceEvent event) {
        try {
            issuanceTransactionalWriter.insertAndIncrement(event);

        } catch (DataIntegrityViolationException e) {
            // 중복 insert시 unique 제약조건에 위배. 이때 스프링에서 제공하는 예외가 Data~Exception
            log.debug(
                    // worker가 재처리하지 않도록 예외를 발생시키지 않고 로그만 남김
                    "UNIQUE 위반은 멱등 처리: couponId = {}, userId = {}",
                    event.getCouponId(),
                    event.getUserId()
            );
        }
    }
}
