package com.example.shopping.controller;

import com.example.shopping.dto.CouponDto;
import com.example.shopping.dto.EventDto;
import com.example.shopping.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;


    @PostMapping
    public ResponseEntity<EventDto.CreateEventResponse> createEvent(@RequestBody EventDto.CreateEventRequest request) {
        EventDto.CreateEventResponse response =  eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto.GetEventInfoResponse> getEventInfo(@PathVariable Long eventId) {
        EventDto.GetEventInfoResponse response = eventService.getEventInfo(eventId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<EventDto.GetEventInfoListResponse> getEventInfoList(@RequestParam int page) {
        EventDto.GetEventInfoListResponse response = eventService.getEventInfoList(page);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventDto.UpdateEventResponse> updateEvent(@PathVariable Long eventId, @RequestBody EventDto.UpdateEventRequest request) {
        EventDto.UpdateEventResponse response = eventService.updateEvent(eventId, request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
