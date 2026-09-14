package com.example.shopping.dto;


import com.example.shopping.entity.Gender;
import com.example.shopping.entity.User;
import com.example.shopping.entity.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class UserDto {

    private final PasswordEncoder passwordEncoder;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @NotBlank(message = "아이디는 필수 입력 항목입니다.")
        private String id;

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        private String password;

        @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
        private String userName;

        @NotBlank(message = "2차 비밀번호는 필수 입력 항목입니다.")
        @Pattern(regexp = "^[0-9]{6}$", message = "2차 비밀번호는 6자리 숫자여야 합니다.")
        private String paymentPassword;
        private String profileImage;
        private Gender gender;
        private Short tall;
        private Short weight;
        private UserType type;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long userId;
        private String id;
        private String userName;
        private Short tall;
        private Short weight;
        private Gender gender;
        private LocalDateTime createdAt;
        private UserType type;

        public static Response fromEntity(User user) {

            return Response.builder()
                    .userId(user.getUserId())
                    .id(user.getId())
                    .userName(user.getUserName())
                    .gender(user.getGender())
                    .tall(user.getTall())
                    .weight(user.getWeight())
                    .createdAt(user.getCreatedAt())
                    .type(user.getType())
                    .build();
        }


    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginRequest {

        @NotBlank(message = "아이디는 필수 입력 항목입니다.")
        private String id;

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        private String password;

    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginResponse {
        private String token;
        private UserType role;
        private String userName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetMyPageResponse {
        private Long orderCount;
        private Long productLikeCount;
        private Long reviewCount;
        private Long postCount;


    }
}