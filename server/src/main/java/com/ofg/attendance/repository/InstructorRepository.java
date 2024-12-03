package com.ofg.attendance.repository;

import com.ofg.attendance.model.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InstructorRepository extends JpaRepository<Instructor, UUID> {
    Instructor findByUserId(UUID userId);
}
