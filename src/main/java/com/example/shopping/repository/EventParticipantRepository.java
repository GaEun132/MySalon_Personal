package com.example.shopping.repository;

import com.example.shopping.entity.EventParticipant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {
    Slice<EventParticipant> findByUserUserId(Long userId, Pageable pageable);

    boolean existsByUserUserIdAndEventEventId(Long userId, Long eventId);
}
