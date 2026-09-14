package com.example.shopping.controller;

import com.example.shopping.dto.IssuanceDto;
import com.example.shopping.security.CurrentUser;
import com.example.shopping.service.IssuanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/issuance")
public class IssuanceController {

    private final IssuanceService issuanceService;

    @PostMapping("/{issuanceId}/use")
    public ResponseEntity<IssuanceDto.UseCouponResponse> useCoupon(@PathVariable Long issuanceId, @CurrentUser Long userId) {
        IssuanceDto.UseCouponResponse response = issuanceService.useCoupon(issuanceId, userId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{userId}/all")
    public ResponseEntity<IssuanceDto.GetAllCouponsByUserResponse> getAllCouponsByUser(@CurrentUser Long userId, @RequestParam int page) {
        IssuanceDto.GetAllCouponsByUserResponse response = issuanceService.getAllCouponsByUser(userId, page);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/used")
    public ResponseEntity<IssuanceDto.GetAllCouponsByUserResponse> getAllUsedCouponsByUser(@CurrentUser Long userId, @RequestParam int page) {
        IssuanceDto.GetAllCouponsByUserResponse response = issuanceService.getAllUsedCouponsByUser(userId, page);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/avail")
    public ResponseEntity<IssuanceDto.GetAllCouponsByUserResponse> getAllAvailableCouponsByUser(@CurrentUser Long userId, @RequestParam int page) {
        IssuanceDto.GetAllCouponsByUserResponse response = issuanceService.getAllAvailableCouponsByUser(userId, page);
        return ResponseEntity.ok(response);
    }

}
