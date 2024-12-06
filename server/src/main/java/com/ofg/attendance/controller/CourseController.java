package com.ofg.attendance.controller;

import com.ofg.attendance.core.util.response.ResponseUtil;
import com.ofg.attendance.core.util.results.ApiDataResponse;
import com.ofg.attendance.core.util.results.ApiResponse;
import com.ofg.attendance.model.request.AssignStudentToCourseRequest;
import com.ofg.attendance.model.request.CourseCreateRequest;
import com.ofg.attendance.model.request.CourseUpdateRequest;
import com.ofg.attendance.model.response.CourseResponse;
import com.ofg.attendance.security.CurrentUser;
import com.ofg.attendance.service.abstracts.CourseService;
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
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    private static final String COURSES_FETCH_SUCCESS = "app.msg.courses.fetch.success";
    private static final String COURSE_FETCH_SUCCESS = "app.msg.course.fetch.success";
    private static final String COURSE_CREATE_SUCCESS = "app.msg.course.create.success";
    private static final String STUDENT_ASSIGN_SUCCESS = "app.msg.student.assign.success";
    private static final String COURSE_UPDATE_SUCCESS = "app.msg.course.update.success";
    private static final String COURSE_DELETE_SUCCESS = "app.msg.course.delete.success";

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<ApiDataResponse<Page<CourseResponse>>> getAllCourses(Pageable pageable) {
        Page<CourseResponse> courses = courseService.getAllCourses(pageable);
        return ResponseUtil.createApiDataResponse(courses, COURSES_FETCH_SUCCESS, HttpStatus.OK);
    }

    @GetMapping("instructor/{instructorId}")
    public ResponseEntity<ApiDataResponse<Page<CourseResponse>>> getAllCoursesByInstructorId(
            @PathVariable UUID instructorId,
            Pageable pageable) {
        Page<CourseResponse> courses = courseService.getAllCoursesByInstructorId(instructorId, pageable);
        return ResponseUtil.createApiDataResponse(courses, COURSES_FETCH_SUCCESS, HttpStatus.OK);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<ApiDataResponse<CourseResponse>> getCourseById(@PathVariable UUID courseId) {
        CourseResponse course = courseService.getCourseResponseById(courseId);
        return ResponseUtil.createApiDataResponse(course, COURSE_FETCH_SUCCESS, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiDataResponse<CourseResponse>> createCourse(
            @AuthenticationPrincipal CurrentUser currentUser,
            @Valid @RequestBody CourseCreateRequest courseCreateRequest) {
        CourseResponse course = courseService.addCourse(currentUser.getId(), courseCreateRequest);
        return ResponseUtil.createApiDataResponse(course, COURSE_CREATE_SUCCESS, HttpStatus.CREATED);
    }

    @PostMapping("/assign-student")
    public ResponseEntity<ApiResponse> assignStudentToCourse(
            @Valid @RequestBody AssignStudentToCourseRequest assignStudentToCourseRequest) {
        courseService.assignStudentToCourse(assignStudentToCourseRequest);
        return ResponseUtil.createApiResponse(STUDENT_ASSIGN_SUCCESS, HttpStatus.CREATED);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<ApiDataResponse<CourseResponse>> updateCourse(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable UUID courseId,
            @Valid @RequestBody CourseUpdateRequest courseUpdateRequest) {
        CourseResponse course = courseService.updateCourse(currentUser.getId(), courseId, courseUpdateRequest);
        return ResponseUtil.createApiDataResponse(course, COURSE_UPDATE_SUCCESS, HttpStatus.OK);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<ApiResponse> deleteCourse(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable UUID courseId) {
        courseService.deleteCourse(currentUser.getId(), courseId);
        return ResponseUtil.createApiResponse(COURSE_DELETE_SUCCESS, HttpStatus.NO_CONTENT);
    }
}
