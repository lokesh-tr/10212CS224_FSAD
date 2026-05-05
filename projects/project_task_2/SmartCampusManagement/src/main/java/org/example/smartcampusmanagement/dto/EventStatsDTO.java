package org.example.smartcampusmanagement.dto;

public class EventStatsDTO {

    private Long eventId;
    private String eventTitle;
    private long totalRegistrations;
    private int maxCapacity;
    private double capacityUtilization;
    private Double averageRating;
    private long totalFeedbacks;

    public EventStatsDTO() {}

    public EventStatsDTO(Long eventId, String eventTitle, long totalRegistrations,
                         int maxCapacity, Double averageRating, long totalFeedbacks) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.totalRegistrations = totalRegistrations;
        this.maxCapacity = maxCapacity;
        this.capacityUtilization = maxCapacity > 0 ? (double) totalRegistrations / maxCapacity * 100 : 0;
        this.averageRating = averageRating;
        this.totalFeedbacks = totalFeedbacks;
    }

    // ─── Getters & Setters ───────────────────────────

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }

    public long getTotalRegistrations() { return totalRegistrations; }
    public void setTotalRegistrations(long totalRegistrations) { this.totalRegistrations = totalRegistrations; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public double getCapacityUtilization() { return capacityUtilization; }
    public void setCapacityUtilization(double capacityUtilization) { this.capacityUtilization = capacityUtilization; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public long getTotalFeedbacks() { return totalFeedbacks; }
    public void setTotalFeedbacks(long totalFeedbacks) { this.totalFeedbacks = totalFeedbacks; }
}
