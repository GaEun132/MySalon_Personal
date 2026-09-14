package com.example.shopping.controller;

import com.example.shopping.dto.CoordiPostDto;
import com.example.shopping.security.CurrentUser;
import com.example.shopping.service.CoordiPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coordi-post")
@RequiredArgsConstructor
public class CoordiPostController {

    private final CoordiPostService coordiPostService;

    @PostMapping
    public ResponseEntity<CoordiPostDto.CreateCoordiPostResponse> createCoordiPost(@CurrentUser Long user, @RequestBody CoordiPostDto.CreateCoordiPostRequest request) {
        CoordiPostDto.CreateCoordiPostResponse createCoordiPostResponse = coordiPostService.createCoordiPost(user,request);
        return ResponseEntity.ok(createCoordiPostResponse);
    }
    @GetMapping
    public ResponseEntity<CoordiPostDto.SimpleCoordiPostListResponse> getAllCoordiPostOrderBy(@CurrentUser Long userId,@RequestParam(required = false) String orderType, @RequestParam int page) {
        CoordiPostDto.SimpleCoordiPostListResponse response = coordiPostService.getAllCoordiPostOrderBy(userId, orderType, page);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top")
    public ResponseEntity<CoordiPostDto.SimpleTop10CoordiPostListResponse> getTop10CoordiPost(@CurrentUser Long userId) {
        CoordiPostDto.SimpleTop10CoordiPostListResponse response = coordiPostService.getTop10CoordiPost(userId);
        return ResponseEntity.ok(response);
    }
}
