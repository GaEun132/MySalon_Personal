package com.example.shopping.repository;

import com.example.shopping.dto.GetAllResponse;
import com.example.shopping.dto.ProductDto;
import com.example.shopping.entity.Product;
import com.example.shopping.entity.QOrderDetail;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.example.shopping.entity.QFavorite.favorite;
import static com.example.shopping.entity.QItem.item;
import static com.example.shopping.entity.QOrder.order;
import static com.example.shopping.entity.QOrderDetail.orderDetail;
import static com.example.shopping.entity.QProduct.product;
import static com.example.shopping.entity.QReview.review;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    public Slice<GetAllResponse> findAllProductByCategoryIdOrderBy(Long userId,List<Long> categoryIds, ProductDto.ProductSearchCondition condition, Pageable pageable) {

        List<GetAllResponse> content = queryFactory
                .select(Projections.fields(GetAllResponse.class,
                    item.product.productId,
                    product.mainImage,
                    product.price,
                    Expressions.numberTemplate(
                            Double.class,
                            "round({0}, 1)",
                            review.score.avg().coalesce(0.0)
                    ).as("score"),
                    review.reviewId.count().castToNum(Integer.class).as("reviewCount"),
                        queryFactory.selectFrom(favorite)
                                .where(favorite.product.eq(product)
                                        .and(favorite.user.userId.eq(userId)))
                                .exists().as("isLiked"))
                )
                .from(item)
                .leftJoin(review).on(item.itemId.eq(review.item.itemId))
                .where(item.product.category.categoryId.in(categoryIds))
                .groupBy(item.product.productId)
                .orderBy(getOrderSpecifier(condition.getSortType()), product.productId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) {
            content.remove(content.size() - 1);
        }


        return new SliceImpl<>(content, pageable, hasNext);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortType) {
        if (sortType == null) {
            return product.productId.desc();
        } else if (sortType.equals("rating")) {
            return review.score.avg().desc();
        } else if (sortType.equals("highPrice")) {
            return product.price.desc();
        } else if (sortType.equals("lowPrice")) {
            return product.price.asc();
        } else {
            return product.productId.desc();

        }

    }
}
