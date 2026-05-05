package com.ticketbooking.jms;

import com.ticketbooking.config.JmsConfig;
import com.ticketbooking.dto.BookingMessage;
import com.ticketbooking.service.BookingService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class BookingConsumer {

    private final BookingService bookingService;

    public BookingConsumer(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @JmsListener(destination = JmsConfig.BOOKING_QUEUE)
    public void receiveBookingMessage(BookingMessage message) {
        System.out.println("[JMS Consumer] Received booking message from queue");
        System.out.println("[JMS Consumer] Processing: UserId=" + message.getUserId()
                + ", EventId=" + message.getEventId()
                + ", Tickets=" + message.getTicketsCount());
        try {
            bookingService.createBooking(
                    message.getUserId(),
                    message.getEventId(),
                    message.getTicketsCount()
            );
            System.out.println("[JMS Consumer] Booking processed successfully");
        } catch (Exception e) {
            System.err.println("[JMS Consumer] Booking processing failed: " + e.getMessage());
        }
    }
}
