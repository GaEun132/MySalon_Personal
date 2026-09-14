package com.example.shopping.dto;

import com.example.shopping.entity.Coupon;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CouponDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateCouponRequest {
        private String name;
        private Integer totalQuantity;
        private Integer issuedQuantity;
        private Integer validityDays;
        private LocalDateTime startsAt;
        private LocalDateTime createdAt;

        public Coupon toEntity() {
            return Coupon.builder()
                    .name(this.name)
                    .totalQuantity(this.totalQuantity)
                    .issuedQuantity(this.issuedQuantity)
                    .validityDays(this.validityDays == null ? 7 : this.validityDays)
                    .startsAt(this.startsAt)
                    .createdAt(this.createdAt)
                    .build();
        }
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateCouponResponse {
        private Long couponId;
        private String name;
        private Integer totalQuantity;
        private Integer issuedQuantity;
        private Integer validityDays;
        private LocalDateTime startsAt;
        private LocalDateTime createdAt;

        public static CreateCouponResponse fromEntity(Coupon coupon) {
            return CreateCouponResponse.builder()
                    .couponId(coupon.getCouponId())
                    .name(coupon.getName())
                    .totalQuantity(coupon.getTotalQuantity())
                    .issuedQuantity(coupon.getIssuedQuantity())
                    .validityDays(coupon.getValidityDays())
                    .startsAt(coupon.getStartsAt())
                    .createdAt(coupon.getCreatedAt())
                    .build();


        }
    }
}
