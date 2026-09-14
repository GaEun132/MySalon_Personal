package com.example.shopping.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;
    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    private Integer maxParticipant;

    @Column(nullable = false)
    @Builder.Default
    private Integer currentParticipant = 0;

    private LocalDateTime createdAt;

    public void changeName(String name) {
        this.name = name;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public void changeMaxParticipant(Integer maxParticipant) {
        this.maxParticipant = maxParticipant;
    }
    public void changeEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }


    public boolean isParticipatingAvailable(LocalDateTime now) {
        return now.isAfter(this.startedAt);
    }
    public boolean isFullBooked() {
        return this.currentParticipant >= this.maxParticipant;
    }
    public boolean isEnded(LocalDateTime time) {
        return time.isAfter(this.endAt);
    }

    public void participate() {
        this.currentParticipant++;
    }





}
