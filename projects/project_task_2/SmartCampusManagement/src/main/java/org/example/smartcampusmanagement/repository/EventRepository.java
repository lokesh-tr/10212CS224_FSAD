package org.example.smartcampusmanagement.repository;

import org.example.smartcampusmanagement.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Upcoming events (date >= today)
    List<Event> findByEventDateGreaterThanEqualOrderByEventDateAsc(LocalDate date);

    // Filter by department
    List<Event> findByDepartmentIgnoreCase(String department);

    // Filter by event type
    List<Event> findByEventTypeIgnoreCase(String eventType);

    // Filter by department and event type
    List<Event> findByDepartmentIgnoreCaseAndEventTypeIgnoreCase(String department, String eventType);

    // Filter by date range
    List<Event> findByEventDateBetweenOrderByEventDateAsc(LocalDate startDate, LocalDate endDate);

    // Search by title (case-insensitive)
    List<Event> findByTitleContainingIgnoreCase(String keyword);

    // Combined search with optional filters
    @Query("SELECT e FROM Event e WHERE " +
           "(:keyword IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:department IS NULL OR LOWER(e.department) = LOWER(:department)) AND " +
           "(:eventType IS NULL OR LOWER(e.eventType) = LOWER(:eventType)) AND " +
           "(:startDate IS NULL OR e.eventDate >= :startDate) AND " +
           "(:endDate IS NULL OR e.eventDate <= :endDate) " +
           "ORDER BY e.eventDate ASC")
    List<Event> searchEvents(@Param("keyword") String keyword,
                             @Param("department") String department,
                             @Param("eventType") String eventType,
                             @Param("startDate") LocalDate startDate,
                             @Param("endDate") LocalDate endDate);

    // Get distinct departments for filter dropdowns
    @Query("SELECT DISTINCT e.department FROM Event e ORDER BY e.department")
    List<String> findDistinctDepartments();

    // Get distinct event types for filter dropdowns
    @Query("SELECT DISTINCT e.eventType FROM Event e ORDER BY e.eventType")
    List<String> findDistinctEventTypes();
}
