package com.ofg.attendance.repository;

import com.ofg.attendance.model.entity.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LectureRepository extends JpaRepository<Lecture, UUID> {
    Optional<Page<Lecture>> findByCourseId(UUID courseId, Pageable pageable);
}
