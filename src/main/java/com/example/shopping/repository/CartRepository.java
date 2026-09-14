package com.example.shopping.repository;

import com.example.shopping.entity.Cart;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c JOIN FETCH c.item i JOIN FETCH i.product p WHERE c.user.userId = :userId order by c.createdAt desc , c.cartId desc")
    Slice<Cart> findByUserUserIdOrderByCreatedAtDes(Long userId, Pageable pageable);

    void deleteByUserUserIdAndCartId(Long userId, Long cartId);

    Optional<Cart> findByUserUserIdAndCartId(Long userId, Long cartId);

    @Query("select c from Cart c join fetch c.item i join fetch i.product p where c.user.userId = :userId and c.isSelected = true order by c.createdAt desc , c.cartId desc")
    Slice<Cart> findAllByUserUserIdAndIsSelectedTrue(Long userId, Pageable pageable);

    void deleteAllByUserUserIdAndCartIdIn(Long userId, List<Long> cartIds);

    boolean existsByUserUserIdAndItemItemId(Long userId, Long itemId);
}
