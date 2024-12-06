package com.ofg.attendance.service.abstracts;

import com.ofg.attendance.model.entity.Course;
import com.ofg.attendance.model.request.AssignStudentToCourseRequest;
import com.ofg.attendance.model.request.CourseCreateRequest;
import com.ofg.attendance.model.request.CourseUpdateRequest;
import com.ofg.attendance.model.response.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CourseService {
    Page<CourseResponse> getAllCourses(Pageable pageable);

    Page<CourseResponse> getAllCoursesByStudentId(UUID userId, UUID studentId, Pageable pageable);

    Page<CourseResponse> getAllCoursesByInstructorId(UUID instructorId, Pageable pageable);

    CourseResponse getCourseResponseById(UUID courseId);

    Course getCourseEntityById(UUID courseId);

    CourseResponse addCourse(UUID userId, CourseCreateRequest createRequest);

    void assignStudentToCourse(AssignStudentToCourseRequest assignStudentToCourseRequest);

    CourseResponse updateCourse(UUID userId, UUID courseId, CourseUpdateRequest CourseUpdateRequest);

    void deleteCourse(UUID userId, UUID courseId);
}
