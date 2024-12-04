package com.ofg.attendance.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CourseCreateRequest(
        @NotBlank(message = "{app.constraint.course-name.not-blank}")
        @Size(min = 3, max = 255)
        String courseName,
        @NotBlank(message = "{app.constraint.course-code.not-blank}")
        @Size(min = 3, max = 255)
        String courseCode
) {

}
