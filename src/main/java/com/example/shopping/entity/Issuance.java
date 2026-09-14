package com.example.shopping.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "issuance",
        uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_id_coupon_id", columnNames = {"user_id", "coupon_id"}
        )
        })

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Issuance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issuance_id")
    private Long issuanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "issuance_fk_user_id"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", foreignKey = @ForeignKey(name = "issuance_fk_coupon_id"))
    private Coupon coupon;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private IssuanceStatus status = IssuanceStatus.ISSUED;

    @Column(nullable = false, updatable = false)
    private LocalDateTime issuedAt; // 발급 시각

    @Column(nullable = false)
    private LocalDateTime expiredAt; //만료 시각

    private LocalDateTime usedAt; // 사용 시각

    public boolean isExpired(LocalDateTime time) {
        return time.isAfter(this.expiredAt);
    }

    public void useCoupon(LocalDateTime time) {
        this.status = IssuanceStatus.USED;
        this.usedAt = time;

    }
}
