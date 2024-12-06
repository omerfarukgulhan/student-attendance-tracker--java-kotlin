package com.ofg.attendance.service.abstracts;

import com.ofg.attendance.model.response.AttendanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AttendanceService {
    Page<AttendanceResponse> getAttendancesByLectureId(UUID userId, UUID lectureId, Pageable pageable);

    Page<AttendanceResponse> getAllAttendancesByCourseId(UUID userId, UUID courseId, Pageable pageable);

    Page<AttendanceResponse> getAllAttendancesByStudentId(UUID userId, Pageable pageable);

    AttendanceResponse getAttendanceById(UUID attendanceId);

    AttendanceResponse markAttendance(UUID userId, String qrContent);

    boolean hasStudentMarkedAttendance(UUID userId, UUID lectureId);
}

