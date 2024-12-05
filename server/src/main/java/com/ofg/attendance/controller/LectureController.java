package com.ofg.attendance.controller;

import com.ofg.attendance.core.util.response.ResponseUtil;
import com.ofg.attendance.core.util.results.ApiDataResponse;
import com.ofg.attendance.core.util.results.ApiResponse;
import com.ofg.attendance.model.request.LectureCreateRequest;
import com.ofg.attendance.model.request.LectureUpdateRequest;
import com.ofg.attendance.model.response.LectureResponse;
import com.ofg.attendance.security.CurrentUser;
import com.ofg.attendance.service.abstracts.LectureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/lectures")
public class LectureController {
    private final LectureService lectureService;

    private static final String LECTURES_FETCH_SUCCESS = "app.msg.lectures.fetch.success";
    private static final String LECTURE_FETCH_SUCCESS = "app.msg.lecture.fetch.success";
    private static final String LECTURE_CREATE_SUCCESS = "app.msg.lecture.create.success";
    private static final String LECTURE_UPDATE_SUCCESS = "app.msg.lecture.update.success";
    private static final String LECTURE_DELETE_SUCCESS = "app.msg.lecture.delete.success";

    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("course/{courseId}")
    public ResponseEntity<ApiDataResponse<Page<LectureResponse>>> getAllLecturesByCourseId(
            @PathVariable UUID courseId,
            Pageable pageable) {
        Page<LectureResponse> lectures = lectureService.getAllLecturesByCourseId(courseId, pageable);
        return ResponseUtil.createApiDataResponse(lectures, LECTURES_FETCH_SUCCESS, HttpStatus.OK);
    }

    @GetMapping("/{lectureId}")
    public ResponseEntity<ApiDataResponse<LectureResponse>> getLectureById(@PathVariable UUID lectureId) {
        LectureResponse lecture = lectureService.getLectureById(lectureId);
        return ResponseUtil.createApiDataResponse(lecture, LECTURE_FETCH_SUCCESS, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiDataResponse<LectureResponse>> createLecture(
            @AuthenticationPrincipal CurrentUser currentUser,
            @Valid @RequestBody LectureCreateRequest lectureCreateRequest) {
        LectureResponse lecture = lectureService.addLecture(currentUser.getId(), lectureCreateRequest);
        return ResponseUtil.createApiDataResponse(lecture, LECTURE_CREATE_SUCCESS, HttpStatus.CREATED);
    }

    @PutMapping("/{lectureId}")
    public ResponseEntity<ApiDataResponse<LectureResponse>> updateLecture(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable UUID lectureId,
            @Valid @RequestBody LectureUpdateRequest lectureUpdateRequest) {
        LectureResponse lecture = lectureService.updateLecture(currentUser.getId(), lectureId, lectureUpdateRequest);
        return ResponseUtil.createApiDataResponse(lecture, LECTURE_UPDATE_SUCCESS, HttpStatus.OK);
    }

    @DeleteMapping("/{lectureId}")
    public ResponseEntity<ApiResponse> deleteLecture(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable UUID lectureId) {
        lectureService.deleteLecture(currentUser.getId(), lectureId);
        return ResponseUtil.createApiResponse(LECTURE_DELETE_SUCCESS, HttpStatus.NO_CONTENT);
    }
}
