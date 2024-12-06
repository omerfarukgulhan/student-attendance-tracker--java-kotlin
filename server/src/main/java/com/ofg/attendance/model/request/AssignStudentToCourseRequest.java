package com.ofg.attendance.model.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignStudentToCourseRequest(
        @NotNull(message = "{app.constraint.user-id.not-null}")
        UUID userId,
        @NotNull(message = "{app.constraint.course-id.not-null}")
        UUID courseId
) {

}
