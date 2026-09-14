package com.example.shopping.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_participant",uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_id_event_id", columnNames = {"user_id", "event_id"}
        )
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventParticipant{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventParticipantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime participatedAt;


}