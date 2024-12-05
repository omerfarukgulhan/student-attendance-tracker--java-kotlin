package com.ofg.attendance.model.response;

import com.ofg.attendance.model.entity.Student;

import java.util.UUID;

public record StudentResponse(
        UUID id,
        UUID userId,
        String enrollmentNumber
) {
    public StudentResponse(Student student) {
        this(student.getId(), student.getUser().getId(),
                student.getEnrollmentNumber());
    }
}
