package com.example.shopping.service;

import com.example.shopping.dto.EventParticipantDto;
import com.example.shopping.entity.Event;
import com.example.shopping.entity.EventParticipant;
import com.example.shopping.entity.User;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.EventParticipantRepository;
import com.example.shopping.repository.EventRepository;
import com.example.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventParticipantService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventParticipantRepository eventParticipantRepository;
    @Transactional
    public EventParticipantDto.CreateEventParticipantResponse createEventParticipant(Long userId, EventParticipantDto.CreateEventParticipantRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        if (!event.isParticipatingAvailable(now)) {
            throw new BusinessException(ErrorCode.EVENT_NOT_STARTED);
        }
        if (event.isFullBooked()) {
            throw new BusinessException(ErrorCode.EVENT_FULL_BOOKED);
        }
        if (event.isEnded(now)) {
            throw new BusinessException(ErrorCode.EVENT_ENDED);
        }
        if (eventParticipantRepository.existsByUserUserIdAndEventEventId(user.getUserId(), event.getEventId())) {
            throw new BusinessException(ErrorCode.EVENT_ALREADY_PARTICIPATED);
        }

        EventParticipant eventParticipant = request.toEntity(user, event);
        EventParticipant savedEventParticipant = eventParticipantRepository.save(eventParticipant);
        return EventParticipantDto.CreateEventParticipantResponse.fromEntity(savedEventParticipant);

    }
    @Transactional
    public EventParticipantDto.UserParticipateEventListResponse getUserParticipateEvents(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Slice<EventParticipant> userEvents = eventParticipantRepository.findByUserUserId(userId, pageable);
        return EventParticipantDto.UserParticipateEventListResponse.fromEntity(userEvents);
    }
    @Transactional
    public void deleteEventParticipant(Long eventParticipantId) {
        if (!eventParticipantRepository.existsById(eventParticipantId)) {
            throw new BusinessException(ErrorCode.EVENT_PARTICIPANT_NOT_FOUND);
        }
        eventParticipantRepository.deleteById(eventParticipantId);
    }
}

