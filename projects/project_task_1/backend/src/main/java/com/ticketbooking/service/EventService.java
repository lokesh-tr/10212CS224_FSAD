package com.ticketbooking.service;

import com.ticketbooking.dto.EventRequest;
import com.ticketbooking.model.Event;
import com.ticketbooking.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public Event createEvent(EventRequest request) {
        Event event = new Event();
        event.setName(request.getName());
        event.setDepartment(request.getDepartment());
        event.setEventDate(request.getEvent_date());
        event.setVenue(request.getVenue());
        event.setPrice(request.getPrice());
        event.setTotalTickets(request.getTotal_tickets());
        event.setAvailableTickets(request.getTotal_tickets());
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
