package org.example.smartcampusmanagement.config;

import org.example.smartcampusmanagement.entity.Event;
import org.example.smartcampusmanagement.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EventRepository eventRepository;

    @Autowired
    public DataInitializer(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) {
        // Only seed if the database is empty
        if (eventRepository.count() > 0) {
            return;
        }

        LocalDate today = LocalDate.now();

        Event[] events = {
            new Event("AI & Machine Learning Workshop",
                "Hands-on workshop covering neural networks, deep learning fundamentals, and real-world ML applications using Python and TensorFlow.",
                today.plusDays(5), LocalTime.of(10, 0),
                "Main Auditorium, Block A", "Computer Science", "Workshop", 120),

            new Event("Annual Cultural Fest: Rhythms 2026",
                "A grand celebration of music, dance, drama, and art featuring performances from students across all departments.",
                today.plusDays(12), LocalTime.of(17, 0),
                "Open Air Theatre", "Cultural Committee", "Festival", 500),

            new Event("Cybersecurity Seminar: Threats & Defenses",
                "Expert-led seminar on modern cybersecurity challenges including ransomware, phishing, and zero-day exploits with live demonstrations.",
                today.plusDays(3), LocalTime.of(14, 0),
                "Seminar Hall 2, Block C", "Information Technology", "Seminar", 80),

            new Event("Entrepreneurship Bootcamp",
                "Two-day intensive bootcamp on building startups, pitch deck creation, fundraising strategies, and lean business models.",
                today.plusDays(20), LocalTime.of(9, 0),
                "Conference Room, Management Block", "Business Administration", "Workshop", 60),

            new Event("Robotics Competition: RoboWars",
                "Inter-college robotics competition where teams design and battle autonomous and remote-controlled robots.",
                today.plusDays(15), LocalTime.of(11, 0),
                "Engineering Lab, Block D", "Mechanical Engineering", "Competition", 200),

            new Event("Data Science with Python",
                "Comprehensive seminar on data analysis, visualization with Matplotlib and Seaborn, and predictive modeling with scikit-learn.",
                today.plusDays(7), LocalTime.of(10, 30),
                "Computer Lab 3, Block B", "Computer Science", "Seminar", 50),

            new Event("Campus Blood Donation Drive",
                "Annual blood donation camp organized in collaboration with the Red Cross. All students and faculty are encouraged to participate.",
                today.plusDays(2), LocalTime.of(9, 0),
                "Health Centre Ground Floor", "Student Welfare", "Social Event", 300),

            new Event("Guest Lecture: Future of Quantum Computing",
                "Distinguished lecture by Dr. Priya Sharma from IISc Bangalore on quantum computing breakthroughs and their industry applications.",
                today.plusDays(10), LocalTime.of(15, 0),
                "Main Auditorium, Block A", "Physics", "Lecture", 150),

            new Event("Spring Boot & Microservices Hands-On",
                "Practical workshop on building production-ready microservices with Spring Boot, Docker, and Kubernetes deployment.",
                today.plusDays(8), LocalTime.of(10, 0),
                "Computer Lab 1, Block B", "Computer Science", "Workshop", 40),

            new Event("Photography & Filmmaking Masterclass",
                "Learn professional photography techniques, video composition, and post-production editing from industry experts.",
                today.plusDays(18), LocalTime.of(14, 0),
                "Media Room, Arts Block", "Fine Arts", "Workshop", 35)
        };

        for (Event event : events) {
            eventRepository.save(event);
        }

        System.out.println("✅ Seeded " + events.length + " sample events into the database.");
    }
}
