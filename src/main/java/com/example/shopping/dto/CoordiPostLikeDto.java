package com.example.shopping.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CoordiPostLikeDto {


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClickCoordiPostLikeRequest {

        @NotNull(message = "게시물 번호는 필수 입력 항목입니다.")
        private Long coordiPostId;

    }
}
