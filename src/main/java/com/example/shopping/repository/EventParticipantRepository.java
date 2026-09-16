package com.example.shopping.repository;

import com.example.shopping.entity.EventParticipant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {
    Slice<EventParticipant> findByUserUserId(Long userId, Pageable pageable);

    boolean existsByUserUserIdAndEventEventId(Long userId, Long eventId);

    @Modifying
    @Query("UPDATE Event e SET e.currentParticipant = e.currentParticipant + 1 WHERE e.eventId = :eventId")
    Integer increaseCurrentParticipant(@Param("eventId") Long eventId);
}
