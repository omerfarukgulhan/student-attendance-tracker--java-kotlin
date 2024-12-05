package com.ofg.attendance.model.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record LectureCreateRequest(
        @NotNull(message = "{app.constraint.course-id.not-null}")
        UUID courseId,
        @Future(message = "{app.constraint.lecture.start-time.must.future}")
        LocalDateTime startTime,
        @Future(message = "{app.constraint.lecture.end-time.must.future}")
        LocalDateTime endTime
) {

}
