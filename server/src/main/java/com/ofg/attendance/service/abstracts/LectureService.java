package com.ofg.attendance.service.abstracts;

import com.ofg.attendance.model.entity.Lecture;
import com.ofg.attendance.model.request.LectureCreateRequest;
import com.ofg.attendance.model.request.LectureUpdateRequest;
import com.ofg.attendance.model.response.LectureResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface LectureService {
    Page<LectureResponse> getAllLecturesByCourseId(UUID courseId, Pageable pageable);

    LectureResponse getLectureResponseById(UUID lectureId);

    Lecture getLectureEntityById(UUID lectureId);

    LectureResponse addLecture(UUID userId, LectureCreateRequest lectureCreateRequest);

    LectureResponse updateLecture(UUID userId, UUID lectureId, LectureUpdateRequest lectureUpdateRequest);

    void deleteLecture(UUID userId, UUID lectureId);
}
