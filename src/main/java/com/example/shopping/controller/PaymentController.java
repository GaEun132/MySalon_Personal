package com.example.shopping.controller;

import com.example.shopping.dto.OrderDto;
import com.example.shopping.dto.PaymentDto;
import com.example.shopping.security.CurrentUser;
import com.example.shopping.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<PaymentDto.GetOrderCheckResponse> getOrderCheckInfoByUser(@CurrentUser Long userId) {
        PaymentDto.GetOrderCheckResponse orders = paymentService.getOrderCheckInfoByUser(userId);
        return ResponseEntity.ok(orders);
    }
}
