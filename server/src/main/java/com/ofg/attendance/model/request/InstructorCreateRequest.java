package com.ofg.attendance.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record InstructorCreateRequest(
        @NotNull(message = "{app.constraint.user-id.not-null}")
        UUID userId,
        @NotBlank(message = "{app.constraint.department.not-blank}")
        @Size(min = 3, max = 255)
        String department
) {

}
