package com.ticketbooking.dto;

public class BookingRequest {
    private Long event_id;
    private Integer tickets_count;

    public Long getEvent_id() { return event_id; }
    public void setEvent_id(Long event_id) { this.event_id = event_id; }
    public Integer getTickets_count() { return tickets_count; }
    public void setTickets_count(Integer tickets_count) { this.tickets_count = tickets_count; }
}
