package com.example.shopping.repository;

import com.example.shopping.dto.CoordiPostDto;
import com.example.shopping.dto.GetAllResponse;
import com.example.shopping.dto.ProductDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.example.shopping.entity.QCoordiPost.coordiPost;
import static com.example.shopping.entity.QCoordiPostLike.coordiPostLike;
import static com.example.shopping.entity.QFavorite.favorite;
import static com.example.shopping.entity.QProduct.product;
import static com.example.shopping.entity.QReview.review;

public class CoordiPostRepositoryImpl implements CoordiPostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CoordiPostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Slice<CoordiPostDto.SimpleCoordiPostResponse> getAllCoordiPostOrderBy(Long userId, String sortType, Pageable pageable) {

        List<CoordiPostDto.SimpleCoordiPostResponse> content = queryFactory
                .select(Projections.fields(CoordiPostDto.SimpleCoordiPostResponse.class,
                        coordiPost.coordiPostId,
                        coordiPost.user.userId,
                        coordiPost.coordiPostImage.as("image"),
                        coordiPost.user.profileImage,
                        coordiPost.title,
                        coordiPost.likeCount,
                        coordiPost.user.userName.as("writerName"),
                        queryFactory.selectFrom(coordiPostLike)
                                .where(coordiPostLike.coordiPost.eq(coordiPost)
                                        .and(coordiPostLike.user.userId.eq(userId)))
                                .exists().as("isLiked"))
                )
                .from(coordiPost)
                .orderBy(getOrderSpecifier(sortType), coordiPost.coordiPostId.desc())
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
            return coordiPost.createdAt.desc();
        } else if (sortType.equals("latest")) {
            return coordiPost.createdAt.desc();
        } else if (sortType.equals("like")) {
            return coordiPost.likeCount.desc();
        }  else {
            return coordiPost.createdAt.desc();
        }

    }



}
