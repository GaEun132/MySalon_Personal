package com.example.shopping.controller;

import com.example.shopping.dto.EventParticipantDto;
import com.example.shopping.entity.EventParticipant;
import com.example.shopping.security.CurrentUser;
import com.example.shopping.service.EventParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event-participant")
@RequiredArgsConstructor
public class EventParticipantController {

    private final EventParticipantService eventParticipantService;

    @PostMapping("/{userId}")
    public ResponseEntity<EventParticipantDto.CreateEventParticipantResponse> createEventParticipant(@PathVariable Long userId, @RequestBody EventParticipantDto.CreateEventParticipantRequest request) {
        EventParticipantDto.CreateEventParticipantResponse response = eventParticipantService.createEventParticipant(userId, request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/v1/{userId}")
    public ResponseEntity<EventParticipantDto.CreateEventParticipantResponse> createEventParticipantWithRedis(@PathVariable Long userId, @RequestBody EventParticipantDto.CreateEventParticipantRequest request) {
        EventParticipantDto.CreateEventParticipantResponse response = eventParticipantService.createEventParticipantWithRedis(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<EventParticipantDto.UserParticipateEventListResponse> getUserParticipateEvents(@CurrentUser Long userId, @RequestParam int page) {
        EventParticipantDto.UserParticipateEventListResponse response = eventParticipantService.getUserParticipateEvents(userId, page);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{eventParticipantId}")
    public ResponseEntity<Void> deleteEventParticipant(@PathVariable Long eventParticipantId) {
        eventParticipantService.deleteEventParticipant(eventParticipantId);
        return ResponseEntity.noContent().build();
    }
}
