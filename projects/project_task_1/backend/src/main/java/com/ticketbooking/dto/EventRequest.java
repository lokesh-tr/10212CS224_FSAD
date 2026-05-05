package com.ticketbooking.dto;

import java.math.BigDecimal;

public class EventRequest {
    private String name;
    private String department;
    private String event_date;
    private String venue;
    private BigDecimal price;
    private Integer total_tickets;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getEvent_date() { return event_date; }
    public void setEvent_date(String event_date) { this.event_date = event_date; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getTotal_tickets() { return total_tickets; }
    public void setTotal_tickets(Integer total_tickets) { this.total_tickets = total_tickets; }
}
