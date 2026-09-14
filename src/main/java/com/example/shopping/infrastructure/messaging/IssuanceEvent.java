package com.example.shopping.infrastructure.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class IssuanceEvent {
    private Long couponId;
    private Long userId;
    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;
}
