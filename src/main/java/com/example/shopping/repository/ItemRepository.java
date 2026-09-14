package com.example.shopping.repository;

import com.example.shopping.entity.Item;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Item i WHERE i.itemId IN :ids")
    List<Item> findAllByIdWithPessimisticLock(List<Long> ids);

    @Query("select i from Item i join fetch i.product p where i.itemId = :itemId")
    Optional<Item> findItemInfoById(Long itemId);
}
