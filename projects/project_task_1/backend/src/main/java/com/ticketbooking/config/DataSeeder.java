package com.ticketbooking.config;

import com.ticketbooking.model.Event;
import com.ticketbooking.model.User;
import com.ticketbooking.repository.EventRepository;
import com.ticketbooking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      EventRepository eventRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Seed admin only if no users exist
        if (userRepository.count() == 0) {
            System.out.println("[Seeder] Seeding admin user...");
            User admin = new User();
            admin.setName("Super Admin");
            admin.setEmail("admin@university.edu");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.admin);
            admin.setDepartment("Administration");
            userRepository.save(admin);

            // Seed a sample student
            User student = new User();
            student.setName("John Student");
            student.setEmail("john@university.edu");
            student.setPasswordHash(passwordEncoder.encode("student123"));
            student.setRole(User.Role.student);
            student.setDepartment("Computer Science");
            userRepository.save(student);

            // Seed a sample faculty
            User faculty = new User();
            faculty.setName("Dr. Smith");
            faculty.setEmail("smith@university.edu");
            faculty.setPasswordHash(passwordEncoder.encode("faculty123"));
            faculty.setRole(User.Role.faculty);
            faculty.setDepartment("Computer Science");
            userRepository.save(faculty);

            System.out.println("[Seeder] Users seeded successfully.");
        }

        // Seed events only if none exist
        if (eventRepository.count() == 0) {
            System.out.println("[Seeder] Seeding events...");

            Event e1 = new Event();
            e1.setName("TechNova 2026");
            e1.setDepartment("Computer Science");
            e1.setEventDate("15 May 2026, 10:00 AM");
            e1.setVenue("Main Auditorium");
            e1.setPrice(new BigDecimal("15.00"));
            e1.setTotalTickets(100);
            e1.setAvailableTickets(100);
            eventRepository.save(e1);

            Event e2 = new Event();
            e2.setName("BioSynergy Symposium");
            e2.setDepartment("Biotechnology");
            e2.setEventDate("22 May 2026, 09:00 AM");
            e2.setVenue("Seminar Hall B");
            e2.setPrice(new BigDecimal("10.00"));
            e2.setTotalTickets(50);
            e2.setAvailableTickets(50);
            eventRepository.save(e2);

            Event e3 = new Event();
            e3.setName("Mechanical Auto Expo");
            e3.setDepartment("Mechanical Engineering");
            e3.setEventDate("10 June 2026, 11:00 AM");
            e3.setVenue("Open Grounds");
            e3.setPrice(new BigDecimal("20.00"));
            e3.setTotalTickets(200);
            e3.setAvailableTickets(200);
            eventRepository.save(e3);

            System.out.println("[Seeder] Events seeded successfully.");
        }
    }
}
