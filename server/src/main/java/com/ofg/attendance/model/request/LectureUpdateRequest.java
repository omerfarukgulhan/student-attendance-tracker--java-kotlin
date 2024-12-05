package com.ofg.attendance.model.request;

import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

public record LectureUpdateRequest(
        @Future(message = "{app.constraint.lecture.start-time.must.future}")
        LocalDateTime startTime,
        @Future(message = "{app.constraint.lecture.end-time.must.future}")
        LocalDateTime endTime
) {

}
