package com.example.shopping.service;

import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CouponIssuer {
    private final StringRedisTemplate redisTemplate;
    // v2: 쿠폰 매진 여부를 레디스에서 조회
    private final RedisScript<Long> script = RedisScript.of(
            new ClassPathResource("lua/issue.lua"), // 스크립트 경로
            Long.class // 리턴 타입
    );
    // v3: 쿠폰 매진 여부, 중복 발급 여부를 레디스에서 조회
    private final RedisScript<Long> scriptWithDuplicateCheck = RedisScript.of(
            new ClassPathResource("lua/issue_with_duplicate_check.lua"),
            Long.class
    );

    public CouponIssuer(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    // v2
    public void tryIssue(Long couponId) {
        // 쿠폰 아이디를 받아 재고를 확인하고 차감
        Long raw = redisTemplate.execute(
                script,
                List.of(stockKey(couponId)) // 첫번쨰 인자에 couponId를 넘김
        );
        if (raw == null) {
            throw new IllegalStateException("Lua 스크립트 결과가 null입니다.");
        }
        // 재고 차감 성공
        if (raw == 1L) {
            return;
        }
        // 재고 차감 실패
        if (raw == 0L) {
            // 재고 부족
            throw new BusinessException(ErrorCode.COUPON_SOLD_OUT);
        }
        throw new IllegalStateException("예상치 못한 Lua 결과: " + raw);
    }
    // v3
    public void tryIssueWithDuplicateCheck( Long couponId, Long userId) {
        // 쿠폰 아이디를 받아 재고를 확인하고 차감
        Long raw = redisTemplate.execute(
                scriptWithDuplicateCheck,
                List.of(stockKey(couponId), usersKey(couponId)), userId.toString() // 첫번째 인자에 [coupon:couponId:stock, coupon:couponId:users]를 넘김. 두번째 인자에 userId를 넘김
        );
        if (raw == null) {
            throw new IllegalStateException("Lua 스크립트 결과가 null입니다.");
        }
        // 재고 차감 성공
        if (raw == 1L) {
            return;
        }
        // 재고 차감 실패
        if (raw == 0L) {
            // 재고 부족
            throw new BusinessException(ErrorCode.COUPON_SOLD_OUT);
        }
        //v3: 중복 발급
        if (raw == -1L) {
            throw new BusinessException(ErrorCode.COUPON_ALREADY_EXIST);
        }
        throw new IllegalStateException("예상치 못한 Lua 결과: " + raw);
    }
    // 쿠폰 정보 생성시 재고를 초기화
    public void initStock(Long couponId, Integer totalQuantity) {
        // set 연산 실행 {key: couponId, value: totalQuantity}
        redisTemplate.opsForValue().set(stockKey(couponId), String.valueOf(totalQuantity));
    }

    // 키 구조: coupon:couponId:stock
    private String stockKey(Long couponId) {
        return "coupon:" + couponId + ":stock";
    }
    // v3
    // 해당 쿠폰을 발급한 사용자 id를 Set에 저장
    private String usersKey(Long couponId) {return "coupon:" + couponId + ":users";}
}
