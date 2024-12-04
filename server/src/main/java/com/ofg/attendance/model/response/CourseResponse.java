package com.ofg.attendance.model.response;

import com.ofg.attendance.model.entity.Course;

import java.util.UUID;

public record CourseResponse(
        UUID id,
        String courseName,
        String courseCode,
        InstructorResponse instructor
) {
    public CourseResponse(Course course) {
        this(course.getId(), course.getCourseName(),
                course.getCourseCode(), new InstructorResponse(course.getInstructor()));
    }
}
