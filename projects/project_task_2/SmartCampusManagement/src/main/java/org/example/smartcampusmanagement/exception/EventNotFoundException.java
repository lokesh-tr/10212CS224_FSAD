package org.example.smartcampusmanagement.exception;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(Long id) {
        super("Event not found with ID: " + id);
    }

    public EventNotFoundException(String message) {
        super(message);
    }
}
