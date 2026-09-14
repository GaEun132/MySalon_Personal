package com.example.shopping.repository;

import com.example.shopping.entity.Review;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    // 컬렉션 페치 조인하고 페이징하기
    // 컬렉션을 페치 조인하면 페이징이 불가능하다. 페이징 + 페치 조인 두가지를 모두 하는 방법은 다음과 같다.
    // 1. toOne 관계를 페치 조인한다.
    // 2. 컬렉션은 지연 로딩으로 조회하며 @BatchSize로 최적화한다.
    @Query("select r from Review r join r.item pd where pd.product.productId = :productId")
    Slice<Review> findAllByProductId(@Param("productId") Long productId, Pageable pageable);

    @Query("select r from Review r join fetch r.item where r.user.userId = :userId order by r.createdAt desc")
    Slice<Review> findAllByUserUserIdOrderByCreatedAtDesc(@Param("userId") Long userId, Pageable pageable);

    @Query("""
    select r from Review r
    join fetch r.item i
    join fetch i.product p
    where p.productId = :productId
    order by r.createdAt desc
    """)
    Slice<Review> findAllByProductIdOrderByCreatedAtDesc(@Param("productId") Long productId, Pageable pageable);


    /*
* select
            r1_0.review_num,
            r1_0.created_at,
            r1_0.product_detail_num,
            r1_0.review_image,
            r1_0.score,
            r1_0.text,
            r1_0.updated_at,
            r1_0.user_num
        from
            reviews r1_0
        join
            product_details pd1_0
                on pd1_0.product_detail_num=r1_0.product_detail_num
        where
            pd1_0.product_num=?
        order by
            r1_0.updated_at desc
        limit
            ?, ?
            * */
    Long countAllByUserUserId(Long userId);

}
