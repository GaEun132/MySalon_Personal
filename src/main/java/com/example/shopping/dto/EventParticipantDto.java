package com.example.shopping.dto;

import com.example.shopping.entity.Event;
import com.example.shopping.entity.EventParticipant;
import com.example.shopping.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

public class EventParticipantDto {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateEventParticipantRequest {
        private Long eventId;

        public EventParticipant toEntity(User user, Event event) {
            return EventParticipant.builder()
                    .event(event)
                    .user(user)
                    .participatedAt(LocalDateTime.now())
                    .build();
        }
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateEventParticipantResponse {
        private Long eventParticipantId;
        private Long eventId;
        private Long userId;
        private LocalDateTime participatedAt;

        public static CreateEventParticipantResponse fromEntity(EventParticipant eventParticipant) {
            return CreateEventParticipantResponse.builder()
                    .eventParticipantId(eventParticipant.getEventParticipantId())
                    .eventId(eventParticipant.getEvent().getEventId())
                    .userId(eventParticipant.getUser().getUserId())
                    .participatedAt(eventParticipant.getParticipatedAt())
                    .build();


        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserParticipateEventListResponse {
        private List<UserParticipateEventResponse> userEvents;
        private boolean hasNext;

        public static UserParticipateEventListResponse fromEntity(Slice<EventParticipant> userEvents) {

            return UserParticipateEventListResponse.builder()
                    .userEvents(userEvents.map(UserParticipateEventResponse::fromEntity).toList())
                    .hasNext(userEvents.hasNext())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserParticipateEventResponse {
        private Long eventParticipantId;
        private Long eventId;
        private String eventName;
        private String description;
        private LocalDateTime startedAt;
        private LocalDateTime endAt;
        private Integer maxParticipant;
        private Integer currentParticipant;
        private Long userId;
        private LocalDateTime participatedAt;

        public static UserParticipateEventResponse fromEntity(EventParticipant eventParticipant) {
            return UserParticipateEventResponse.builder()
                    .eventParticipantId(eventParticipant.getEventParticipantId())
                    .eventId(eventParticipant.getEvent().getEventId())
                    .eventName(eventParticipant.getEvent().getName())
                    .description(eventParticipant.getEvent().getDescription())
                    .startedAt(eventParticipant.getEvent().getStartedAt())
                    .endAt(eventParticipant.getEvent().getEndAt())
                    .maxParticipant(eventParticipant.getEvent().getMaxParticipant())
                    .currentParticipant(eventParticipant.getEvent().getCurrentParticipant())
                    .userId(eventParticipant.getUser().getUserId())
                    .participatedAt(eventParticipant.getParticipatedAt())
                    .build();

        }
    }





}
