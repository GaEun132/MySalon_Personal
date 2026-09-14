package com.example.shopping.service;

import com.example.shopping.dto.EventDto;
import com.example.shopping.entity.Event;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    @Transactional
    public EventDto.CreateEventResponse createEvent(EventDto.CreateEventRequest request) {
        Event event = request.toEntity();
        Event savedEvent = eventRepository.save(event);
        return EventDto.CreateEventResponse.fromEntity(savedEvent);
    }
    @Transactional(readOnly = true)
    public EventDto.GetEventInfoResponse getEventInfo(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_NOT_FOUND));
        return EventDto.GetEventInfoResponse.fromEntity(event);
    }
    @Transactional
    public EventDto.UpdateEventResponse updateEvent(Long eventId, EventDto.UpdateEventRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_NOT_FOUND));
        if (request.getName() != null) {
            event.changeName(request.getName());;
        }
        if (request.getDescription() != null) {
            event.changeDescription(request.getDescription());
        }
        if (request.getStartedAt() != null) {
            event.changeStartedAt(request.getStartedAt());
        }
        if (request.getEndAt() != null) {
            event.changeEndAt(request.getEndAt());
        }
        if (request.getMaxParticipant() != null) {
            event.changeMaxParticipant(request.getMaxParticipant());
        }
        Event updatedEvent = eventRepository.save(event);
        return EventDto.UpdateEventResponse.fromEntity(updatedEvent);

    }
    @Transactional
    public void deleteEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new BusinessException(ErrorCode.EVENT_NOT_FOUND);
        }
        eventRepository.deleteById(eventId);
    }
    @Transactional(readOnly = true)
    public EventDto.GetEventInfoListResponse getEventInfoList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Slice<Event> events = eventRepository.findAll(pageable);
        return EventDto.GetEventInfoListResponse.fromEntity(events);
    }
}
