package com.example.shopping.dto;

import com.example.shopping.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

public class EventDto {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateEventRequest {

        private String name;
        private String description;
        private LocalDateTime startedAt;
        private LocalDateTime endAt;
        private Integer maxParticipant;
        private Integer currentParticipant;

        public Event toEntity() {
            return Event.builder()
                    .name(name)
                    .description(description)
                    .startedAt(startedAt)
                    .maxParticipant(maxParticipant)
                    .currentParticipant(currentParticipant)
                    .endAt(endAt).endAt(endAt)
                    .createdAt(LocalDateTime.now())
                    .build();
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateEventResponse {

        private Long eventId;
        private String name;
        private String description;
        private LocalDateTime startedAt;
        private LocalDateTime endAt;
        private Integer maxParticipant;
        private Integer currentParticipant;
        private LocalDateTime createdAt;

        public static CreateEventResponse fromEntity(Event event) {
            return CreateEventResponse.builder()
                    .eventId(event.getEventId())
                    .name(event.getName())
                    .description(event.getDescription())
                    .startedAt(event.getStartedAt())
                    .endAt(event.getEndAt())
                    .maxParticipant(event.getMaxParticipant())
                    .currentParticipant(event.getCurrentParticipant())
                    .createdAt(event.getCreatedAt())
                    .build();
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetEventInfoResponse {

        private Long eventId;
        private String name;
        private String description;
        private LocalDateTime startedAt;
        private LocalDateTime endAt;
        private Integer maxParticipant;
        private Integer currentParticipant;
        private LocalDateTime createdAt;

        public static GetEventInfoResponse fromEntity(Event event) {
            return GetEventInfoResponse.builder()
                    .eventId(event.getEventId())
                    .name(event.getName())
                    .description(event.getDescription())
                    .startedAt(event.getStartedAt())
                    .endAt(event.getEndAt())
                    .maxParticipant(event.getMaxParticipant())
                    .currentParticipant(event.getCurrentParticipant())
                    .createdAt(event.getCreatedAt())
                    .build();
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateEventRequest {

        private String name;
        private String description;
        private LocalDateTime startedAt;
        private LocalDateTime endAt;
        private Integer maxParticipant;
        private Integer currentParticipant;
        private LocalDateTime createdAt;


    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateEventResponse {

        private Long eventId;
        private String name;
        private String description;
        private LocalDateTime startedAt;
        private LocalDateTime endAt;
        private Integer maxParticipant;
        private Integer currentParticipant;
        private LocalDateTime createdAt;

        public static UpdateEventResponse fromEntity(Event event) {
            return UpdateEventResponse.builder()
                    .eventId(event.getEventId())
                    .name(event.getName())
                    .description(event.getDescription())
                    .startedAt(event.getStartedAt())
                    .endAt(event.getEndAt())
                    .maxParticipant(event.getMaxParticipant())
                    .currentParticipant(event.getCurrentParticipant())
                    .createdAt(event.getCreatedAt())
                    .build();
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static  class GetEventInfoListResponse {
        private List<GetEventInfoResponse> eventList;
        private boolean hasNext;

        public static GetEventInfoListResponse fromEntity(Slice<Event> events) {
            List<GetEventInfoResponse> eventInfoList = events.stream()
                    .map(GetEventInfoResponse::fromEntity)
                    .toList();
            return GetEventInfoListResponse.builder()
                    .eventList(eventInfoList)
                    .hasNext(events.hasNext())
                    .build();
        }
    }
}
