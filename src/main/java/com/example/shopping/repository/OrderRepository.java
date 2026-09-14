package com.example.shopping.repository;


import com.example.shopping.entity.Order;
import com.example.shopping.entity.OrderDetail;
import com.example.shopping.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUserUserId(Long userId, Pageable pageable);

    @Query(value = """
        SELECT * FROM (
            SELECT od.*, 
                   ROW_NUMBER() OVER (PARTITION BY od.order_id ORDER BY od.order_detail_id ASC) as rn
            FROM order_detail od 
            WHERE od.order_id IN :orderIds
        ) ranked
        WHERE ranked.rn <= 2
        """, nativeQuery = true)
    List<OrderDetail> findTop2OrderDetailsByOrderIds(@Param("orderIds") List<Long> orderIds);;
    // fetch join을 사용하려 했으나 jpql이 아닌 네이티브 쿼리이기 때문에 fetch join을 사용할 수 없다.
    // jpql을 사용할 수 없는 이유
    // 1. ROW_NUMBER() OVER (PARTITION BY ...) 미지원
    // 2. FROM 절의 서브쿼리(Inline View) 미지원

    // 대안: 주문 ID 5개를 가지고 IN 절로 모든 주문 상세를 JPQL로 일단 긁어온 뒤, Java 코드로 주문당 2개씩만 남기고 필터링(필터링 작업은 애플리케이션에서 수행)
    @Query("""
    SELECT od 
    FROM OrderDetail od 
    JOIN FETCH od.item pd
    JOIN FETCH pd.product p
    WHERE od.order.orderId IN :orderIds
    """)
    List<OrderDetail> findAllOrderDetailsByOrderIds(@Param("orderIds") List<Long> orderIds);

    List<Order> user(User user);

    Long countAllByUserUserId(Long userId);
}
