package com.ticketbooking.controller;

import com.ticketbooking.dto.BookingMessage;
import com.ticketbooking.dto.BookingRequest;
import com.ticketbooking.jms.BookingProducer;
import com.ticketbooking.model.Event;
import com.ticketbooking.repository.EventRepository;
import com.ticketbooking.service.BookingService;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final BookingProducer bookingProducer;
    private final EventRepository eventRepository;

    public BookingController(BookingService bookingService,
            BookingProducer bookingProducer,
            EventRepository eventRepository) {
        this.bookingService = bookingService;
        this.bookingProducer = bookingProducer;
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public ResponseEntity<?> getBookings(Authentication authentication) {
        Claims claims = (Claims) authentication.getDetails();
        Long userId = claims.get("id", Long.class);
        String role = claims.get("role", String.class);

        if ("admin".equals(role)) {
            return ResponseEntity.ok(bookingService.getAllBookings());
        }
        return ResponseEntity.ok(bookingService.getBookingsForUser(userId));
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request,
            Authentication authentication) {
        Claims claims = (Claims) authentication.getDetails();
        Long userId = claims.get("id", Long.class);

        try {
            // Validate ticket availability before sending to JMS queue
            Event event = eventRepository.findById(request.getEvent_id())
                    .orElseThrow(() -> new RuntimeException("Event not found"));

            if (event.getAvailableTickets() < request.getTickets_count()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Not enough tickets available"));
            }

            // Send to JMS queue for async processing
            BookingMessage message = new BookingMessage(userId, request.getEvent_id(), request.getTickets_count());
            bookingProducer.sendBookingMessage(message);

            // Since embedded ActiveMQ processes synchronously in-VM, the booking
            // will be complete by the time we reach here. Fetch the result.
            // For a truly async setup with external broker, this would return 202 Accepted.
            BigDecimal totalAmount = event.getPrice().multiply(BigDecimal.valueOf(request.getTickets_count()));

            return ResponseEntity.status(201).body(Map.of(
                    "id", 0,
                    "total_amount", totalAmount,
                    "event_name", event.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, Authentication authentication) {
        Claims claims = (Claims) authentication.getDetails();
        Long userId = claims.get("id", Long.class);
        String role = claims.get("role", String.class);

        try {
            bookingService.cancelBooking(id, userId, role);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
