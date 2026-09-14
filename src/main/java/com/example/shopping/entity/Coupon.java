package com.example.shopping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer totalQuantity; // 총 발급수량

    @Column(nullable = false)
    private Integer issuedQuantity; //현재 발급된 수량

    @Column(nullable = false)
    @Builder.Default
    private Integer validityDays = 7;

    @Column
    private LocalDateTime startsAt; // 발급 시작 시각

    @Column(nullable = false)
    private LocalDateTime createdAt; // 생성 시각

    public boolean isBookingAvailable(LocalDateTime now) {
        return now.isAfter(this.startsAt);
    }
    public boolean isSoldOut() {
        return this.issuedQuantity >= totalQuantity;
    }

    public void issue() {
        this.issuedQuantity++;
    }
}
