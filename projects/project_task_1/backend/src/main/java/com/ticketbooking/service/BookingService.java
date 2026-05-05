package com.ticketbooking.service;

import com.ticketbooking.model.Booking;
import com.ticketbooking.model.Event;
import com.ticketbooking.model.User;
import com.ticketbooking.repository.BookingRepository;
import com.ticketbooking.repository.EventRepository;
import com.ticketbooking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository,
                          EventRepository eventRepository,
                          UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public List<Map<String, Object>> getBookingsForUser(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserIdOrderByBookingDateDesc(userId);
        return bookings.stream().map(this::mapBookingForUser).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAllByOrderByBookingDateDesc();
        return bookings.stream().map(this::mapBookingForAdmin).collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> createBooking(Long userId, Long eventId, Integer ticketsCount) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getAvailableTickets() < ticketsCount) {
            throw new RuntimeException("Not enough tickets available");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BigDecimal totalAmount = event.getPrice().multiply(BigDecimal.valueOf(ticketsCount));

        event.setAvailableTickets(event.getAvailableTickets() - ticketsCount);
        eventRepository.save(event);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setEvent(event);
        booking.setTicketsCount(ticketsCount);
        booking.setTotalAmount(totalAmount);
        booking.setBookingDate(LocalDateTime.now());
        bookingRepository.save(booking);

        Map<String, Object> result = new HashMap<>();
        result.put("id", booking.getId());
        result.put("total_amount", totalAmount);
        result.put("event_name", event.getName());
        return result;
    }

    @Transactional
    public void cancelBooking(Long bookingId, Long userId, String userRole) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getId().equals(userId) && !"admin".equals(userRole)) {
            throw new RuntimeException("Access denied");
        }

        Event event = booking.getEvent();
        event.setAvailableTickets(event.getAvailableTickets() + booking.getTicketsCount());
        eventRepository.save(event);

        bookingRepository.delete(booking);
    }

    private Map<String, Object> mapBookingForUser(Booking b) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", b.getId());
        map.put("event_id", b.getEvent().getId());
        map.put("event_name", b.getEvent().getName());
        map.put("tickets_count", b.getTicketsCount());
        map.put("total_amount", b.getTotalAmount());
        map.put("booking_date", b.getBookingDate());
        return map;
    }

    private Map<String, Object> mapBookingForAdmin(Booking b) {
        Map<String, Object> map = mapBookingForUser(b);
        map.put("user_name", b.getUser().getName());
        map.put("user_email", b.getUser().getEmail());
        return map;
    }
}
