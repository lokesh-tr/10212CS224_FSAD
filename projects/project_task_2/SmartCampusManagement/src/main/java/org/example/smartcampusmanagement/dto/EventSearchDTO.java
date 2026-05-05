package org.example.smartcampusmanagement.dto;

import java.time.LocalDate;

public class EventSearchDTO {

    private String keyword;
    private String department;
    private String eventType;
    private LocalDate startDate;
    private LocalDate endDate;

    public EventSearchDTO() {}

    // ─── Getters & Setters ───────────────────────────

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
