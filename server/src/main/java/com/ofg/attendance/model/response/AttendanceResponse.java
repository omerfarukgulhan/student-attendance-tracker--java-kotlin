package com.ofg.attendance.model.response;

import com.ofg.attendance.model.entity.Attendance;

import java.util.UUID;

public record AttendanceResponse(
        UUID id,
        StudentResponse student,
        LectureResponse lecture
) {
    public AttendanceResponse(Attendance attendance) {
        this(attendance.getId(), new StudentResponse(attendance.getStudent()),
                new LectureResponse(attendance.getLecture()));
    }
}
