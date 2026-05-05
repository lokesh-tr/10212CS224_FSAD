package org.example.smartcampusmanagement.service;

import org.example.smartcampusmanagement.entity.Event;
import org.example.smartcampusmanagement.entity.Registration;
import org.example.smartcampusmanagement.exception.EventNotFoundException;
import org.example.smartcampusmanagement.exception.RegistrationException;
import org.example.smartcampusmanagement.repository.EventRepository;
import org.example.smartcampusmanagement.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public RegistrationService(RegistrationRepository registrationRepository,
                               EventRepository eventRepository) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Registration registerForEvent(Long eventId, Registration registration) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        // Check for duplicate registration
        if (registrationRepository.existsByStudentEmailIgnoreCaseAndEventId(
                registration.getStudentEmail(), eventId)) {
            throw new RegistrationException("You have already registered for this event.");
        }

        // Check capacity
        if (event.getRegisteredCount() >= event.getMaxCapacity()) {
            throw new RegistrationException("This event is full. No more registrations accepted.");
        }

        registration.setEvent(event);
        Registration saved = registrationRepository.save(registration);

        // Increment registered count
        event.setRegisteredCount(event.getRegisteredCount() + 1);
        eventRepository.save(event);

        return saved;
    }

    public List<Registration> getRegistrationsByEvent(Long eventId) {
        return registrationRepository.findByEventId(eventId);
    }

    public List<Registration> getRegistrationsByEmail(String email) {
        return registrationRepository.findByStudentEmailIgnoreCase(email);
    }

    public long getRegistrationCount(Long eventId) {
        return registrationRepository.countByEventId(eventId);
    }
}
