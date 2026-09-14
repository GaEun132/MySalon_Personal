package com.example.shopping.service;

import com.example.shopping.dto.IssuanceDto;
import com.example.shopping.entity.Coupon;
import com.example.shopping.entity.Issuance;
import com.example.shopping.entity.IssuanceStatus;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.IssuanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssuanceService {

    private final IssuanceRepository issuanceRepository;

    @Transactional
    public IssuanceDto.UseCouponResponse useCoupon(Long issuanceId, Long userId) {

        Issuance issuance = issuanceRepository.findById(issuanceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ISSUANCE_NOT_FOUND));
        IssuanceStatus status = issuance.getStatus();
        if (status == IssuanceStatus.USED) {
            throw new BusinessException(ErrorCode.COUPON_ALREADY_USED);
        } else if (status == IssuanceStatus.EXPIRED) {
            throw new BusinessException(ErrorCode.COUPON_EXPIRED);
        }
        LocalDateTime now = LocalDateTime.now();
        if (issuance.isExpired(now)) {
            throw new BusinessException(ErrorCode.COUPON_EXPIRED);
        }
        issuance.useCoupon(now);
        return IssuanceDto.UseCouponResponse.fromEntity(issuance);


    }
    @Transactional(readOnly = true)
    public IssuanceDto.GetAllCouponsByUserResponse getAllCouponsByUser(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Slice<Issuance> issuances = issuanceRepository.findByUserUserIdOrderByIssuedAtDesc(userId, pageable);
        return IssuanceDto.GetAllCouponsByUserResponse.fromEntity(issuances);
    }
    @Transactional(readOnly = true)
    public IssuanceDto.GetAllCouponsByUserResponse getAllUsedCouponsByUser(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Slice<Issuance> issuances = issuanceRepository.findByUserUserIdAndStatus_USEDAndStatus_EXPIREDOrderByIssuedAtDescIssuanceIdDesc(userId, pageable);
        return IssuanceDto.GetAllCouponsByUserResponse.fromEntity(issuances);
    }
    @Transactional(readOnly = true)
    public IssuanceDto.GetAllCouponsByUserResponse getAllAvailableCouponsByUser(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Slice<Issuance> issuances = issuanceRepository.findByUserUserIdAndStatus_ISSUEDOrderByIssuedAtDescIssuanceIdDesc(userId, pageable);
        return IssuanceDto.GetAllCouponsByUserResponse.fromEntity(issuances);
    }

    @Transactional
    public Issuance getMaxDiscountCoupon(Long userId) {
        return issuanceRepository.findByUserUserIdAndStatusIsIssued(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ISSUANCE_NOT_FOUND));
    }
}
