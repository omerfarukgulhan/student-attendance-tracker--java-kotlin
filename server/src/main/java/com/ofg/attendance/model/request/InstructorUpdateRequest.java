package com.ofg.attendance.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InstructorUpdateRequest(
        @NotBlank(message = "{app.constraint.department.not-blank}")
        @Size(min = 3, max = 255)
        String department
) {

}
