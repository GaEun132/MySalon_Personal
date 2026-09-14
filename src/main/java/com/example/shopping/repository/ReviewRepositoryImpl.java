package com.example.shopping.repository;

import com.example.shopping.dto.ReviewDto;
import com.example.shopping.dto.SortType;
import com.example.shopping.entity.Review;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.util.StringUtils;

import java.util.List;
import static com.example.shopping.entity.QItem.item;
import static com.example.shopping.entity.QReview.review;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public Slice<Review> findFilteredReviews(Long productId,String color, ReviewDto.ReviewSearchCondition condition, Pageable pageable) {

        List<Review> content =  queryFactory
                .selectFrom(review)
                .join(review.item, item).fetchJoin()
                .where(item.product.productId.eq(productId),
                        item.color.eq(color),
                        sizeEq(condition.getSize()),
                        heightBetween(condition.getMinHeight(), condition.getMaxHeight()),
                        weightBetween(condition.getMinWeight(), condition.getMaxWeight())

                )
                .orderBy(getOrderSpecifier(condition.getSortType()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) {
            content.remove(content.size() - 1);
        }
        return new SliceImpl<>(content, pageable, hasNext);

    }

    private BooleanExpression sizeEq(String size) {
        return StringUtils.hasText(size) ? review.item.size.eq(size) : null;
    }

    private BooleanExpression heightBetween(Integer min, Integer max) {
        if (min == null && max == null) return null;
        if (min == null) return review.height.loe(max);
        if (max == null) return review.height.goe(min);
        return review.height.between(min, max);
    }
    private BooleanExpression weightBetween(Integer min, Integer max) {
        if (min == null && max == null) return null;
        if (min == null) return review.weight.loe(max);
        if (max == null) return review.weight.goe(min);
        return review.weight.between(min, max);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortType) {
        if (sortType!=null) {
            if (sortType.equals("rating"))
                return review.score.desc();
        }
        return review.createdAt.desc();
    }

}
