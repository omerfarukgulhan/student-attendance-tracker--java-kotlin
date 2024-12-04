package com.ofg.attendance.repository;

import com.ofg.attendance.model.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    Page<Course> findAllByInstructorId(UUID instructorId, Pageable pageable);
}
