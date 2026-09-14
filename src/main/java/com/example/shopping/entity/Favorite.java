package com.example.shopping.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorite",uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_id_product_id", columnNames = {"user_id", "product_id"}
        )
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long favoriteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "favorite_fk_user_id"), nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "favorite_fk_product_id"), nullable = false)
    private Product product;

    private LocalDateTime createdAt;
}
