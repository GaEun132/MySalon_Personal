package com.example.shopping.entity;

import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private Integer count;

    @Column(nullable = true)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "item_fk_product_id"))
    private Product product;

    // N쪽은 한방향 연관관계만 설정
    public void assignProduct(Product product) {
        this.product = product;
    }

    public void addStock(int quantity) {
        this.count += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.count - quantity;
        if (restStock < 0) {
            throw new BusinessException(ErrorCode.STOCK_NOT_ENOUGH);
        }
        this.count = restStock;
    }


    public void changeSize(String size) {
        this.size = size;
    }
    public void changeColor(String color) {
        this.color = color;
    }

    public void changeCount(Integer count) {
        this.count = count;
    }

    public void changeImage(String image) {
        this.image = image;
    }
}
