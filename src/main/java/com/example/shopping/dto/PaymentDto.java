package com.example.shopping.dto;

import com.example.shopping.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

public class PaymentDto {


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetOrderCheckResponse {
        private Long addressId;
        private String addressName;
        private String city;
        private String street;
        private String zipcode;
        private List<CartDto.GetUserCartResponse> carts;
        private boolean hasNext;
        private Long issuanceId;
        private Long userId;
        private Long couponId;
        private String couponName;
        private LocalDateTime expiredAt;

        public static GetOrderCheckResponse fromEntity(Address address, Slice<Cart> selectedCarts, Issuance issuance) {
            return GetOrderCheckResponse.builder()
                    .addressId(address.getId())
                    .addressName(address.getName())
                    .city(address.getCity())
                    .street(address.getStreet())
                    .zipcode(address.getZipcode())
                    .carts(selectedCarts.stream().map(CartDto.GetUserCartResponse::fromEntity).toList())
                    .hasNext(selectedCarts.hasNext())
                    .issuanceId(issuance.getIssuanceId())
                    .userId(issuance.getUser().getUserId())
                    .couponId(issuance.getCoupon().getCouponId())
                    .couponName(issuance.getCoupon().getName())
                    .expiredAt(issuance.getExpiredAt())
                    .build();
        }
    }


}
