package com.ofg.attendance.repository;

import com.ofg.attendance.model.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    Optional<Page<Attendance>> findByLectureId(UUID lectureId, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Attendance a " +
            "WHERE a.student.id = :studentId AND a.lecture.id = :lectureId")
    boolean existsByStudentIdAndLectureId(@Param("studentId") UUID studentId,
                                          @Param("lectureId") UUID lectureId);
}
