package com.example.shopping.controller;


import com.example.shopping.dto.FavoriteDto;
import com.example.shopping.security.CurrentUser;
import com.example.shopping.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // 하트 클릭
    @PostMapping("/{productId}")
    public ResponseEntity<FavoriteDto.ClickFavoriteResponse> clickFavorite(@CurrentUser Long userId, @PathVariable Long productId) {
        FavoriteDto.ClickFavoriteResponse response = favoriteService.clickFavorite(userId, productId);
        return ResponseEntity.ok(response);
    }

    // 유저별 찜 목록 조회
    @GetMapping("/user")
    public ResponseEntity<FavoriteDto.GetUserFavoriteListResponse> getUserFavorite(@CurrentUser Long userId, int page) {
        FavoriteDto.GetUserFavoriteListResponse favorites = favoriteService.getUserFavorite(userId, page);
        return ResponseEntity.ok(favorites);
    }

    // 유저가 찜한 상품 개수 조회
    @GetMapping("/user/{userNum}/count")
    public ResponseEntity<Long> getUserFavoriteCount(@PathVariable Long userNum) {
        return ResponseEntity.ok(favoriteService.getUserFavoriteCount(userNum));
    }

    // 특정 상품 찜 개수 조회
    @GetMapping("/product/{productNum}/count")
    public ResponseEntity<Long> getProductFavoriteCount(@PathVariable Long productNum) {
        return ResponseEntity.ok(favoriteService.getProductFavoriteCount(productNum));
    }
}
