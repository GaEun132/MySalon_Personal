package com.example.shopping.service;

import com.example.shopping.dto.PaymentDto;
import com.example.shopping.entity.Address;
import com.example.shopping.entity.Cart;
import com.example.shopping.entity.Issuance;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.AddressRepository;
import com.example.shopping.repository.CartRepository;
import com.example.shopping.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final IssuanceService issuanceService;

    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final CouponRepository couponRepository;
    @Transactional
    public PaymentDto.GetOrderCheckResponse getOrderCheckInfoByUser(Long userId) {

        // 기본주소 조회
        Address address = addressRepository.findByUserUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));
        // 장바구니 조회
        Pageable pageable = PageRequest.of(0, 5);
        Slice<Cart> selectedCarts = cartRepository.findAllByUserUserIdAndIsSelectedTrue(userId, pageable);
        // 최대 할인율 쿠폰 조회
        Issuance issuance =issuanceService.getMaxDiscountCoupon(userId);

        return PaymentDto.GetOrderCheckResponse.fromEntity(address, selectedCarts, issuance);

    }
}
