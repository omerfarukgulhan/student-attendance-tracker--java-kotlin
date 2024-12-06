package com.ofg.attendance.service.abstracts;

import com.ofg.attendance.model.response.AttendanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AttendanceService {
    Page<AttendanceResponse> getAttendancesByLectureId(UUID lectureId, Pageable pageable);

    AttendanceResponse getAttendanceById(UUID attendanceId);

    AttendanceResponse markAttendance(UUID userId, String qrContent);

    boolean hasStudentMarkedAttendance(UUID userId, UUID lectureId);
}

