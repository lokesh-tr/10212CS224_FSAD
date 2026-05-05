package org.example.smartcampusmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Student name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "student_name", nullable = false)
    private String studentName;

    @NotNull(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(name = "student_email", nullable = false)
    private String studentEmail;

    @NotNull(message = "Student ID is required")
    @Size(min = 2, max = 20, message = "Student ID must be between 2 and 20 characters")
    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    @Column(name = "phone")
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "registered_at", updatable = false)
    private LocalDateTime registeredAt;

    @PrePersist
    protected void onCreate() {
        this.registeredAt = LocalDateTime.now();
    }

    // ─── Constructors ────────────────────────────────

    public Registration() {}

    public Registration(String studentName, String studentEmail, String studentId, String phone, Event event) {
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentId = studentId;
        this.phone = phone;
        this.event = event;
    }

    // ─── Getters & Setters ───────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }
}
