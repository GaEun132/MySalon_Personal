package com.example.shopping.infrastructure.messaging;

import com.example.shopping.entity.Coupon;
import com.example.shopping.entity.Issuance;
import com.example.shopping.entity.IssuanceStatus;
import com.example.shopping.entity.User;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.CouponRepository;
import com.example.shopping.repository.IssuanceRepository;
import com.example.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class IssuanceTransactionalWriter { // DB 처리 로직 수행

    private final IssuanceRepository issuanceRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;

    @Transactional
    public void insertAndIncrement(IssuanceEvent event) {

        Coupon coupon = couponRepository.findById(event.getCouponId())
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));
        User user = userRepository.findById(event.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        issuanceRepository.save(Issuance.builder()
                .user(user)
                .coupon(coupon)
                .status(IssuanceStatus.ISSUED)
                .issuedAt(event.getIssuedAt())
                .expiredAt(event.getExpiredAt())
                .build());
        couponRepository.incrementIssuedQuantity(event.getCouponId());

    }

}
