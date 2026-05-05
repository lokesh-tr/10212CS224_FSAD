package org.example.smartcampusmanagement.controller;

import jakarta.validation.Valid;
import org.example.smartcampusmanagement.dto.EventSearchDTO;
import org.example.smartcampusmanagement.dto.EventStatsDTO;
import org.example.smartcampusmanagement.entity.Event;
import org.example.smartcampusmanagement.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventRestController {

    private final EventService eventService;

    @Autowired
    public EventRestController(EventService eventService) {
        this.eventService = eventService;
    }

    // ─── GET all events ─────────────────────────────

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    // ─── GET single event ───────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    // ─── GET upcoming events ────────────────────────

    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        return ResponseEntity.ok(eventService.getUpcomingEvents());
    }

    // ─── GET search events ──────────────────────────

    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvents(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        EventSearchDTO searchDTO = new EventSearchDTO();
        searchDTO.setKeyword(keyword);
        searchDTO.setDepartment(department);
        searchDTO.setEventType(eventType);
        if (startDate != null && !startDate.isBlank()) {
            searchDTO.setStartDate(java.time.LocalDate.parse(startDate));
        }
        if (endDate != null && !endDate.isBlank()) {
            searchDTO.setEndDate(java.time.LocalDate.parse(endDate));
        }

        return ResponseEntity.ok(eventService.searchEvents(searchDTO));
    }

    // ─── POST create event ──────────────────────────

    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        Event created = eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ─── PUT update event ───────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @Valid @RequestBody Event event) {
        return ResponseEntity.ok(eventService.updateEvent(id, event));
    }

    // ─── DELETE event ───────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    // ─── GET event stats ────────────────────────────

    @GetMapping("/{id}/stats")
    public ResponseEntity<EventStatsDTO> getEventStats(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventStats(id));
    }
}
