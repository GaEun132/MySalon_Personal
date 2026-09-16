package com.example.shopping.service;

import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventIssuer {
    private final StringRedisTemplate redisTemplate;

    // 이벤트 참가 인원 증가용 Lua 스크립트
    private final RedisScript<Long> script = RedisScript.of(
            new ClassPathResource("lua/event_participate.lua"),
            Long.class
    );

    public EventIssuer(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void tryParticipate(Long eventId) {
        Long raw = redisTemplate.execute(
                script,
                List.of(eventKey(eventId))
        );

        if (raw == null) {
            throw new IllegalStateException("이벤트 Lua 스크립트 결과가 null입니다.");
        }
        if (raw == 1L) {
            return; // 참가 성공
        }
        if (raw == 0L) {
            throw new BusinessException(ErrorCode.EVENT_FULL_BOOKED); // 정원 마감
        }
        throw new IllegalStateException("예상치 못한 Lua 결과: " + raw);
    }
    // 쿠폰 정보 생성시 재고를 초기화
    public void initCapacity(Long eventId, Integer capacity) {
        // set 연산 실행 {key: event:eventId:capacity, value: capacity}
        redisTemplate.opsForValue().set(eventKey(eventId), String.valueOf(capacity));
    }

    private String eventKey(Long eventId) {
        return "event:" + eventId + ":capacity";
    }
}