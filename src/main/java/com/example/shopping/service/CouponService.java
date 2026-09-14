package com.example.shopping.service;

import com.example.shopping.dto.CouponDto;
import com.example.shopping.dto.IssuanceDto;
import com.example.shopping.entity.Coupon;
import com.example.shopping.entity.Issuance;
import com.example.shopping.entity.IssuanceStatus;
import com.example.shopping.entity.User;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.infrastructure.messaging.InMemoryIssuanceQueue;
import com.example.shopping.infrastructure.messaging.IssuanceEvent;
import com.example.shopping.repository.CouponRepository;
import com.example.shopping.repository.IssuanceRepository;
import com.example.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final IssuanceRepository issuanceRepository;
    // v2: 레디스 도입
    private final CouponIssuer couponIssuer;
    // v3: 인메모리 큐 도입
    private final InMemoryIssuanceQueue issuanceQueue;
    // v4: 스프링에서 제공하는 큐
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public CouponDto.CreateCouponResponse createCoupon(CouponDto.CreateCouponRequest request) {

        Coupon savedCoupon = couponRepository.save(request.toEntity());

        return CouponDto.CreateCouponResponse.fromEntity(savedCoupon);
    }

    @Transactional // v2: 쿠폰 매진 여부를 레디스에서 조회
    public CouponDto.CreateCouponResponse createCouponWithRedis(CouponDto.CreateCouponRequest request) {

        Coupon savedCoupon = couponRepository.save(request.toEntity());
        // v2: 쿠폰 생성시 { key: coupon:couponId:stock, value: totalQuantity} 를 레디스에 저장
        couponIssuer.initStock(savedCoupon.getCouponId(), savedCoupon.getTotalQuantity());
        return CouponDto.CreateCouponResponse.fromEntity(savedCoupon);
    }



    @Transactional
    public IssuanceDto.IssueResponse issueCoupon(Long userId, Long couponId) {

/*         Coupon coupon = couponRepository.findById(couponId)
                 .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));*/
        // 비관적 락 적용
        Coupon coupon = couponRepository.findByIdWithPessimisticLock(couponId)
                 .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));
         User user = userRepository.findById(userId)
                 .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 쿠폰이 발급 가능한지 검증
        LocalDateTime now = LocalDateTime.now();
        if (!coupon.isBookingAvailable(now)) {
         throw new BusinessException(ErrorCode.COUPON_NOT_FOUND);
        }
        if (coupon.isSoldOut()) {
         throw new BusinessException(ErrorCode.COUPON_SOLD_OUT);
        }
        if (issuanceRepository.existsByUserUserIdAndCouponCouponId(userId, couponId)) {
        throw new BusinessException(ErrorCode.COUPON_ALREADY_EXIST);
        }
        coupon.issue();
        Issuance issuance = Issuance.builder()
             .user(user)
             .coupon(coupon)
             .issuedAt(now)
             .expiredAt(now.plusDays(coupon.getValidityDays()))
             .build();
        Issuance saveIssuance = issuanceRepository.save(issuance);
        return IssuanceDto.IssueResponse.fromEntity(saveIssuance);

    }

    @Transactional // v2: 쿠폰 매진 여부를 레디스에서 조회
    public IssuanceDto.IssueResponse issueCouponWithRedis(Long userId, Long couponId) {
        // v2: 여기서 조회한 coupon의 발급 수량을 차감하는 것이 아님.
        // v2: 비관적 락이 메소드 전체에 걸리는 것이 아니기 때문에 임계 영역이 줄어듦.
        Coupon coupon = couponRepository.findById(couponId)
                 .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));
         // v2: redis로 coupon 발급 수량을 조회하고 차감하기 때문에 비관적 락이 필요없다.
         /*        Coupon coupon = couponRepository.findByIdWithPessimisticLock(couponId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));*/
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 쿠폰이 발급 가능한지 검증
        LocalDateTime now = LocalDateTime.now();
        if (!coupon.isBookingAvailable(now)) {
            throw new BusinessException(ErrorCode.COUPON_NOT_FOUND);
        }
        // v2: 쿠폰 매진 여부도 레디스에서 검사하고 있으므로 필요 없음
/*        if (coupon.isSoldOut()) {
            throw new BusinessException(ErrorCode.COUPON_SOLD_OUT);
        }*/
        if (issuanceRepository.existsByUserUserIdAndCouponCouponId(userId, couponId)) {
            throw new BusinessException(ErrorCode.COUPON_ALREADY_EXIST);
        }
        // v2: 레디스에서 쿠폰 수량 확인 후 차감
        couponIssuer.tryIssue(couponId);
        // v2: db에서 쿠폰 수량을 조회하고 차감하는 쿼리를 원자적으로 수행
        couponRepository.incrementIssuedQuantity(couponId);

        // v2: db에서 조회한 쿠폰을 그대로 사용하면 db 조회 시점에 락이 걸려있지 않기 떄문에 동시성 문제가 발생함.
        // v2: db에서 조회후 issueQuantity를 증가시키는 것이 아니라 조회하지 않고 바로 증가시키도록 수정.
        //coupon.issue();
        Issuance issuance = Issuance.builder()
                .user(user)
                .coupon(coupon)
                .issuedAt(now)
                .expiredAt(now.plusDays(coupon.getValidityDays()))
                .build();
        Issuance saveIssuance = issuanceRepository.save(issuance);
        return IssuanceDto.IssueResponse.fromEntity(saveIssuance);

    }

    @Transactional // v3: 쿠폰 매진 여부, 중복 발급 여부를 레디스에서 조회, 큐로 비동기 처리
    public IssuanceDto.IssueResponse issueCouponWithRedisDuplicateCheck(Long userId, Long couponId) {
        // v2: 여기서 조회한 coupon의 발급 수량을 차감하는 것이 아님.
        // v2: 비관적 락이 메소드 전체에 걸리는 것이 아니기 때문에 임계 영역이 줄어듦.
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));
        // v2: redis로 coupon 발급 수량을 조회하고 차감하기 때문에 비관적 락이 필요없다.
         /*        Coupon coupon = couponRepository.findByIdWithPessimisticLock(couponId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));*/
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 쿠폰이 발급 가능한지 검증
        LocalDateTime now = LocalDateTime.now();
        if (!coupon.isBookingAvailable(now)) {
            throw new BusinessException(ErrorCode.COUPON_NOT_FOUND);
        }
        // v2: 쿠폰 매진 여부도 레디스에서 검사하고 있으므로 필요 없음
/*        if (coupon.isSoldOut()) {
            throw new BusinessException(ErrorCode.COUPON_SOLD_OUT);
        }*/

        // v3: 레디스에서 중복 검사를 하므로 제거
        /*        if (issuanceRepository.existsByUserUserIdAndCouponCouponId(userId, couponId)) {
            throw new BusinessException(ErrorCode.COUPON_ALREADY_EXIST);
        }*/
        // v3: 레디스에서 쿠폰 수량 확인 후 차감, 중복 발급 판단도 레디스에서 수행
        couponIssuer.tryIssueWithDuplicateCheck(couponId, userId);

        // v3: 여기서부터 큐로 비동기처리
        LocalDateTime expiredAt = now.plusDays(coupon.getValidityDays());
        issuanceQueue.enqueue(new IssuanceEvent(couponId, userId, LocalDateTime.now(), expiredAt));


/*        // v2: db에서 쿠폰 수량을 조회하고 차감하는 쿼리를 원자적으로 수행
        couponRepository.incrementIssuedQuantity(couponId);

        // v2: db에서 조회한 쿠폰을 그대로 사용하면 db 조회 시점에 락이 걸려있지 않기 떄문에 동시성 문제가 발생함.
        // v2: db에서 조회후 issueQuantity를 증가시키는 것이 아니라 조회하지 않고 바로 증가시키도록 수정.
        //coupon.issue();
        Issuance issuance = Issuance.builder()
                .user(user)
                .coupon(coupon)
                .issuedAt(now)
                .expiredAt(now.plusDays(coupon.getValidityDays()))
                .build();
        Issuance saveIssuance = issuanceRepository.save(issuance);*/
        return IssuanceDto.IssueResponse.fromEvent(userId, couponId, IssuanceStatus.ISSUED, now, expiredAt);

    }


    @Transactional // v4: 스프링 큐로 변경
    public IssuanceDto.IssueResponse issueCouponWithSpringQueue(Long userId, Long couponId) {
        // v2: 여기서 조회한 coupon의 발급 수량을 차감하는 것이 아님.
        // v2: 비관적 락이 메소드 전체에 걸리는 것이 아니기 때문에 임계 영역이 줄어듦.
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));
        // v2: redis로 coupon 발급 수량을 조회하고 차감하기 때문에 비관적 락이 필요없다.
         /*        Coupon coupon = couponRepository.findByIdWithPessimisticLock(couponId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));*/
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 쿠폰이 발급 가능한지 검증
        LocalDateTime now = LocalDateTime.now();
        if (!coupon.isBookingAvailable(now)) {
            throw new BusinessException(ErrorCode.COUPON_NOT_FOUND);
        }
        // v2: 쿠폰 매진 여부도 레디스에서 검사하고 있으므로 필요 없음
/*        if (coupon.isSoldOut()) {
            throw new BusinessException(ErrorCode.COUPON_SOLD_OUT);
        }*/

        // v3: 레디스에서 중복 검사를 하므로 제거
        /*        if (issuanceRepository.existsByUserUserIdAndCouponCouponId(userId, couponId)) {
            throw new BusinessException(ErrorCode.COUPON_ALREADY_EXIST);
        }*/
        // v3: 레디스에서 쿠폰 수량 확인 후 차감, 중복 발급 판단도 레디스에서 수행
        couponIssuer.tryIssueWithDuplicateCheck(couponId, userId);

        // v3: 여기서부터 큐로 비동기처리
        LocalDateTime expiredAt = now.plusDays(coupon.getValidityDays());
        // v4: 스프링 큐로 변경
        eventPublisher.publishEvent(new IssuanceEvent(couponId, userId, LocalDateTime.now(), expiredAt));
        //issuanceQueue.enqueue(new IssuanceEvent(couponId, userId, LocalDateTime.now(), expiredAt));


/*        // v2: db에서 쿠폰 수량을 조회하고 차감하는 쿼리를 원자적으로 수행
        couponRepository.incrementIssuedQuantity(couponId);

        // v2: db에서 조회한 쿠폰을 그대로 사용하면 db 조회 시점에 락이 걸려있지 않기 떄문에 동시성 문제가 발생함.
        // v2: db에서 조회후 issueQuantity를 증가시키는 것이 아니라 조회하지 않고 바로 증가시키도록 수정.
        //coupon.issue();
        Issuance issuance = Issuance.builder()
                .user(user)
                .coupon(coupon)
                .issuedAt(now)
                .expiredAt(now.plusDays(coupon.getValidityDays()))
                .build();
        Issuance saveIssuance = issuanceRepository.save(issuance);*/
        return IssuanceDto.IssueResponse.fromEvent(userId, couponId, IssuanceStatus.ISSUED, now, expiredAt);

    }

}
