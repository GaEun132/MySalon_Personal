package com.example.shopping.controller;


import com.example.shopping.dto.UserDto;
import com.example.shopping.security.CurrentUser;
import com.example.shopping.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    // 유저 생성
    @PostMapping("/sign-up")
    public ResponseEntity<UserDto.Response> createUser(@Valid @RequestBody UserDto.Request request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    // 유저 수정
    @PutMapping("/edit-profile")
    public ResponseEntity<UserDto.Response> editUser(
            @CurrentUser Long userId,
            @Valid @RequestBody UserDto.Request request
    ) {
        return ResponseEntity.ok(userService.editUser(userId, request));
    }

    // 유저 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // 모든 유저 조회!!@!
    @GetMapping
    public ResponseEntity<List<UserDto.Response>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // 유저 넘으로 유저 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto.Response> getUserByUserId(@PathVariable Long userId) {
        UserDto.Response userResponse = userService.getUserByUserId(userId);
        return ResponseEntity.ok(userResponse);
    }

    // 로그인한 유저의 정보 조회
    @GetMapping("/user-info")
    public ResponseEntity<UserDto.Response> getUserInfo(@CurrentUser Long userId) {
        UserDto.Response userResponse = userService.getUserByUserId(userId);
        return ResponseEntity.ok(userResponse);
    }

    //jwt 토큰 생성
    @PostMapping("/login")
    public ResponseEntity<UserDto.LoginResponse>  authenticateAndGetToken(@RequestBody UserDto.LoginRequest authRequest) {
        UserDto.LoginResponse userResponse = userService.login(authRequest);
        return ResponseEntity.ok(userResponse);
    }
    @GetMapping("/my-pagae")
    public ResponseEntity<UserDto.GetMyPageResponse> getMyPageInfo(@CurrentUser Long userId) {
        UserDto.GetMyPageResponse response = userService.getMyPageInfo(userId);
        return ResponseEntity.ok(response);
    }
}
