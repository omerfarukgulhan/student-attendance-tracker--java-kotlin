package com.ofg.attendance.model.response;

import com.ofg.attendance.model.entity.Instructor;

import java.util.UUID;

public record InstructorResponse(
        UUID id,
        UserResponseWithoutRoles user,
        String department
) {
    public InstructorResponse(Instructor instructor) {
        this(instructor.getId(), new UserResponseWithoutRoles(instructor.getUser()),
                instructor.getDepartment());
    }
}
