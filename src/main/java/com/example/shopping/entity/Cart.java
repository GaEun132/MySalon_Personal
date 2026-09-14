package com.example.shopping.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart",uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_id_item_id", columnNames = {"user_id", "item_id"}
        )
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "cart_fk_item_id"))
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "cart_fk_user_id"))
    private User user;

    private Integer quantity;

    private boolean isSelected;

    LocalDateTime createdAt;

    public void changeIsSelected() {
        this.isSelected = !this.isSelected;
    }


}
