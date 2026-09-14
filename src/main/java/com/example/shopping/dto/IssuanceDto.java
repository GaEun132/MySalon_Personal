package com.example.shopping.dto;

import com.example.shopping.entity.Coupon;
import com.example.shopping.entity.Issuance;
import com.example.shopping.entity.IssuanceStatus;
import com.example.shopping.entity.User;
import com.example.shopping.infrastructure.messaging.IssuanceEvent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

public class IssuanceDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class IssueResponse {

        private Long issuanceId;
        private Long userId;
        private Long couponId;
        private IssuanceStatus issuanceStatus;
        private LocalDateTime issuedAt;
        private LocalDateTime expiredAt;

        public static IssuanceDto.IssueResponse fromEntity(Issuance issuance) {

            return IssueResponse.builder()
                    .issuanceId(issuance.getIssuanceId())
                    .userId(issuance.getUser().getUserId())
                    .couponId(issuance.getCoupon().getCouponId())
                    .issuanceStatus(issuance.getStatus())
                    .issuedAt(issuance.getIssuedAt())
                    .expiredAt(issuance.getExpiredAt())
                    .build();
        }
        // v3: 이벤트의 속성값을 그대로 반환
        public static IssuanceDto.IssueResponse fromEvent(Long userId, Long couponId, IssuanceStatus issuanceStatus, LocalDateTime issuedAt, LocalDateTime expiredAt) {

            return IssueResponse.builder()
                    .userId(userId)
                    .couponId(couponId)
                    .issuanceStatus(issuanceStatus)
                    .issuedAt(issuedAt)
                    .expiredAt(expiredAt)
                    .build();
        }


    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UseCouponResponse {

        private Long issuanceId;
        private Long userId;
        private Long couponId;
        private IssuanceStatus issuanceStatus;
        private LocalDateTime issuedAt;
        private LocalDateTime expiredAt;
        private LocalDateTime usedAt;

        public static UseCouponResponse fromEntity(Issuance issuance) {
            return UseCouponResponse.builder()
                    .issuanceId(issuance.getIssuanceId())
                    .userId(issuance.getUser().getUserId())
                    .couponId(issuance.getCoupon().getCouponId())
                    .issuanceStatus(issuance.getStatus())
                    .issuedAt(issuance.getIssuedAt())
                    .expiredAt(issuance.getExpiredAt())
                    .usedAt(issuance.getUsedAt())
                    .build();
        }
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GetAllCouponsByUserResponse {
        private boolean hasNext;
        List<CouponInfo> coupons;

        public static GetAllCouponsByUserResponse fromEntity(Slice<Issuance> issuances) {
            List<CouponInfo> couponInfos = issuances.stream()
                    .map(issuance -> new CouponInfo().fromEntity(issuance))
                    .toList();
            return GetAllCouponsByUserResponse.builder()
                    .hasNext(issuances.hasNext())
                    .coupons(couponInfos)
                    .build();
        }

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CouponInfo {
        private Long couponId;
        private Long issuanceId;
        private Long userId;
        private String name;
        private Integer validityDays;
        private IssuanceStatus status;
        private LocalDateTime issuedAt;
        private LocalDateTime expiredAt;
        private LocalDateTime usedAt;

        public CouponInfo fromEntity(Issuance issuance) {
            return CouponInfo.builder()
                    .couponId(issuance.getCoupon().getCouponId())
                    .issuanceId(issuance.getIssuanceId())
                    .userId(issuance.getUser().getUserId())
                    .name(issuance.getCoupon().getName())
                    .validityDays(issuance.getCoupon().getValidityDays())
                    .status(issuance.getStatus())
                    .issuedAt(issuance.getIssuedAt())
                    .expiredAt(issuance.getExpiredAt())
                    .usedAt(issuance.getUsedAt())
                    .build();
        }

    }

}
