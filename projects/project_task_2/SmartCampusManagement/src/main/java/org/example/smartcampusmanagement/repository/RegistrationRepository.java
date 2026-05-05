package org.example.smartcampusmanagement.repository;

import org.example.smartcampusmanagement.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    List<Registration> findByEventId(Long eventId);

    List<Registration> findByStudentEmailIgnoreCase(String studentEmail);

    long countByEventId(Long eventId);

    boolean existsByStudentEmailIgnoreCaseAndEventId(String studentEmail, Long eventId);
}
