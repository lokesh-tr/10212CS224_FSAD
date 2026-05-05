package com.ticketbooking.dto;

import java.io.Serializable;

public class BookingMessage implements Serializable {
    private Long userId;
    private Long eventId;
    private Integer ticketsCount;

    public BookingMessage() {}

    public BookingMessage(Long userId, Long eventId, Integer ticketsCount) {
        this.userId = userId;
        this.eventId = eventId;
        this.ticketsCount = ticketsCount;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public Integer getTicketsCount() { return ticketsCount; }
    public void setTicketsCount(Integer ticketsCount) { this.ticketsCount = ticketsCount; }
}
