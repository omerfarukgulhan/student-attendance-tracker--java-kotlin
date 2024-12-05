package com.ofg.attendance.model.response;

import com.ofg.attendance.model.entity.Lecture;

import java.time.LocalDateTime;
import java.util.UUID;

public record LectureResponse(
        UUID id,
        CourseResponse course,
        QRCodeResponse qrCode,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public LectureResponse(Lecture lecture) {
        this(lecture.getId(), new CourseResponse(lecture.getCourse()),
                new QRCodeResponse(lecture.getQrCode()),
                lecture.getStartTime(), lecture.getEndTime());
    }
}
