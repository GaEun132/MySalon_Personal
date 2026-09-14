package com.example.shopping.entity;

import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", foreignKey = @ForeignKey(name = "product_fk_user_id"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "product_fk_category_id"))
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    private String mainImage;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long deliveryPrice;

    @Column(nullable = false)
    @Builder.Default
    private Long likeCount = 0L;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Item> items = new ArrayList<>();

    // 1쪽은 양방향 연관관계 설정
    public void assignItem(Item item) {
        items.add(item);
        item.assignProduct(this);
    }

    public void changeUser(User user) {
        this.user = user;
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public void changePrice(Integer price) {
        this.price = price;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeDeliveryPrice(Long deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public void changeItem(Long itemId, String size, String color, Integer count, String image) {
        for (Item item : items) {
            if (item.getItemId().equals(itemId)) {
                item.changeSize(size);
                item.changeColor(color);
                item.changeCount(count);
                item.changeImage(image);
                return;
            }
        }
        throw new BusinessException(ErrorCode.ITEM_NOT_FOUND);
    }

    public void removeItem(Item item) {
        this.getItems().remove(item);
        item.assignProduct(null);
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }
    public void decreaseLikeCount() {
        this.likeCount--;
    }
}