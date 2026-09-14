package com.example.shopping.controller;


import com.example.shopping.dto.CoordiPostDto;
import com.example.shopping.security.CurrentUser;
import com.example.shopping.service.CoordiPostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coordi-post-like")
@RequiredArgsConstructor
public class CoordiPostLikeController {

    private final CoordiPostLikeService coordiPostLikeService;

    @PostMapping("/{coordiPostId}")
    public ResponseEntity<CoordiPostDto.ClickCoordiPostLikeResponse> clickLike(@CurrentUser Long userId, @PathVariable Long coordiPostId) {
        CoordiPostDto.ClickCoordiPostLikeResponse response = coordiPostLikeService.clickPost(userId, coordiPostId);
        return ResponseEntity.ok(response);
    }

}
