package com.example.shopping.controller;

import com.example.shopping.dto.CouponDto;
import com.example.shopping.dto.IssuanceDto;
import com.example.shopping.security.CurrentUser;
import com.example.shopping.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponDto.CreateCouponResponse> createCoupon(@RequestBody CouponDto.CreateCouponRequest request) {
        CouponDto.CreateCouponResponse response =  couponService.createCoupon(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/redis")
    public ResponseEntity<CouponDto.CreateCouponResponse> createCouponWithRedis(@RequestBody CouponDto.CreateCouponRequest request) {
        CouponDto.CreateCouponResponse response =  couponService.createCouponWithRedis(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{couponId}/issue")
    public ResponseEntity<IssuanceDto.IssueResponse> issueCoupon(@CurrentUser Long userId, @PathVariable Long couponId) {
        IssuanceDto.IssueResponse response = couponService.issueCoupon(userId, couponId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("/{couponId}/issue/redis")
    public ResponseEntity<IssuanceDto.IssueResponse> issueCouponWithRedis(@CurrentUser Long userId, @PathVariable Long couponId) {
        IssuanceDto.IssueResponse response = couponService.issueCouponWithRedis(userId, couponId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
}
