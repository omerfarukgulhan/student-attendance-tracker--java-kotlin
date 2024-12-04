package com.ofg.attendance.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CourseUpdateRequest(
        @NotBlank(message = "{app.constraint.course-name.not-blank}")
        @Size(min = 3, max = 255)
        String courseName
) {

}
