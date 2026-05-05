package com.ticketbooking.jms;

import com.ticketbooking.config.JmsConfig;
import com.ticketbooking.dto.BookingMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookingProducer {

    private final JmsTemplate jmsTemplate;

    public BookingProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendBookingMessage(BookingMessage message) {
        System.out.println("[JMS Producer] Sending booking message to queue: " + JmsConfig.BOOKING_QUEUE);
        System.out.println("[JMS Producer] UserId=" + message.getUserId()
                + ", EventId=" + message.getEventId()
                + ", Tickets=" + message.getTicketsCount());
        jmsTemplate.convertAndSend(JmsConfig.BOOKING_QUEUE, message);
    }
}
