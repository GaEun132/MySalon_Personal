package com.example.shopping.controller;


import com.example.shopping.dto.OrderDto;
import com.example.shopping.security.CurrentUser;
import com.example.shopping.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAuthority('SELLER') or hasAuthority('BUYER')")
    public ResponseEntity<OrderDto.OrderCompleteResponse> createOrder(@CurrentUser Long userId, @RequestBody OrderDto.CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request, userId));
    }

    @GetMapping("/user/before")
    @PreAuthorize("hasAuthority('SELLER') or hasAuthority('BUYER')")
    public ResponseEntity<OrderDto.OrderHistoryListResponse> getAllOrdersByUserBefore(@CurrentUser Long userId, @RequestParam int page) {
        OrderDto.OrderHistoryListResponse orders = orderService.getOrderHistoryBefore(userId, page);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/after")
    @PreAuthorize("hasAuthority('SELLER') or hasAuthority('BUYER')")
    public ResponseEntity<OrderDto.OrderHistoryListResponse> getAllOrdersByUser(@CurrentUser Long userId, @RequestParam int page) {
        OrderDto.OrderHistoryListResponse orders = orderService.getOrderHistory(userId, page);
        return ResponseEntity.ok(orders);
    }


}
