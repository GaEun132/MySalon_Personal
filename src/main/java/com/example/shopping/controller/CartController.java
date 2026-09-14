package com.example.shopping.controller;

import com.example.shopping.dto.CartDto;
import com.example.shopping.security.CurrentUser;
import com.example.shopping.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;


    @PostMapping
    public ResponseEntity<CartDto.CreateCartResponse> createCart(@CurrentUser Long userId, @RequestBody CartDto.CreateCartRequest request) {
        CartDto.CreateCartResponse response = cartService.createCart(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CartDto.GetUserCartListResponse> getUserCart(@CurrentUser Long userId, @RequestParam int page) {
        CartDto.GetUserCartListResponse response = cartService.getUserCart(userId, page);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{cartId}")
    public ResponseEntity<CartDto.UpdateIsSelectedResponse> updateIsSelected(@CurrentUser Long userId, @PathVariable Long cartId) {
        CartDto.UpdateIsSelectedResponse response = cartService.updateIsSelected(userId, cartId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@CurrentUser Long userId, @PathVariable Long cartId) {
        cartService.deleteCart(userId, cartId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/order")
    public ResponseEntity<Void> deleteCartAfterOrder(@CurrentUser Long userId,@RequestBody CartDto.DeleteCartAfterOrderRequest request) {
        cartService.deleteCartAfterOrder(userId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/selected")
    public ResponseEntity<CartDto.GetUserCartListResponse> getUserSelectedCart(@CurrentUser Long userId, @RequestParam int page) {
        CartDto.GetUserCartListResponse response = cartService.getUserSelectedCart(userId, page);
        return ResponseEntity.ok(response);
    }

}
