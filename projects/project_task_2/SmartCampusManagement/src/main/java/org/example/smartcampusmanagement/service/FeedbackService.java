package org.example.smartcampusmanagement.service;

import org.example.smartcampusmanagement.entity.Event;
import org.example.smartcampusmanagement.entity.Feedback;
import org.example.smartcampusmanagement.exception.EventNotFoundException;
import org.example.smartcampusmanagement.repository.EventRepository;
import org.example.smartcampusmanagement.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final EventRepository eventRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository,
                           EventRepository eventRepository) {
        this.feedbackRepository = feedbackRepository;
        this.eventRepository = eventRepository;
    }

    public Feedback submitFeedback(Long eventId, Feedback feedback) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        feedback.setEvent(event);
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getFeedbackForEvent(Long eventId) {
        return feedbackRepository.findByEventIdOrderBySubmittedAtDesc(eventId);
    }

    public Double getAverageRating(Long eventId) {
        return feedbackRepository.findAverageRatingByEventId(eventId);
    }
}
