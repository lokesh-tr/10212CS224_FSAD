package org.example.smartcampusmanagement.service;

import org.example.smartcampusmanagement.dto.EventSearchDTO;
import org.example.smartcampusmanagement.dto.EventStatsDTO;
import org.example.smartcampusmanagement.entity.Event;
import org.example.smartcampusmanagement.exception.EventNotFoundException;
import org.example.smartcampusmanagement.repository.EventRepository;
import org.example.smartcampusmanagement.repository.FeedbackRepository;
import org.example.smartcampusmanagement.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final FeedbackRepository feedbackRepository;

    @Autowired
    public EventService(EventRepository eventRepository,
                        RegistrationRepository registrationRepository,
                        FeedbackRepository feedbackRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.feedbackRepository = feedbackRepository;
    }

    // ─── CRUD Operations ────────────────────────────

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        Event existing = getEventById(id);
        existing.setTitle(updatedEvent.getTitle());
        existing.setDescription(updatedEvent.getDescription());
        existing.setEventDate(updatedEvent.getEventDate());
        existing.setEventTime(updatedEvent.getEventTime());
        existing.setVenue(updatedEvent.getVenue());
        existing.setDepartment(updatedEvent.getDepartment());
        existing.setEventType(updatedEvent.getEventType());
        existing.setMaxCapacity(updatedEvent.getMaxCapacity());
        return eventRepository.save(existing);
    }

    public void deleteEvent(Long id) {
        Event event = getEventById(id);
        eventRepository.delete(event);
    }

    // ─── Query Operations ───────────────────────────

    public List<Event> getUpcomingEvents() {
        return eventRepository.findByEventDateGreaterThanEqualOrderByEventDateAsc(LocalDate.now());
    }

    public List<Event> searchEvents(EventSearchDTO searchDTO) {
        String keyword = (searchDTO.getKeyword() != null && searchDTO.getKeyword().isBlank())
                ? null : searchDTO.getKeyword();
        String department = (searchDTO.getDepartment() != null && searchDTO.getDepartment().isBlank())
                ? null : searchDTO.getDepartment();
        String eventType = (searchDTO.getEventType() != null && searchDTO.getEventType().isBlank())
                ? null : searchDTO.getEventType();

        return eventRepository.searchEvents(
                keyword,
                department,
                eventType,
                searchDTO.getStartDate(),
                searchDTO.getEndDate()
        );
    }

    public List<String> getAllDepartments() {
        return eventRepository.findDistinctDepartments();
    }

    public List<String> getAllEventTypes() {
        return eventRepository.findDistinctEventTypes();
    }

    // ─── Statistics ─────────────────────────────────

    public EventStatsDTO getEventStats(Long eventId) {
        Event event = getEventById(eventId);
        long totalRegistrations = registrationRepository.countByEventId(eventId);
        Double avgRating = feedbackRepository.findAverageRatingByEventId(eventId);
        long totalFeedbacks = feedbackRepository.countByEventId(eventId);

        return new EventStatsDTO(
                event.getId(),
                event.getTitle(),
                totalRegistrations,
                event.getMaxCapacity(),
                avgRating,
                totalFeedbacks
        );
    }

    public List<EventStatsDTO> getAllEventStats() {
        return getAllEvents().stream()
                .map(event -> getEventStats(event.getId()))
                .toList();
    }
}
