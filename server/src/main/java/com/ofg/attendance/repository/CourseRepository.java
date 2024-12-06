package com.ofg.attendance.repository;

import com.ofg.attendance.model.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    Page<Course> findAllByInstructorId(UUID instructorId, Pageable pageable);

    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId")
    Optional<Page<Course>> findAllByStudentId(@Param("studentId") UUID studentId, Pageable pageable);
}
