package com.ofg.attendance.service.abstracts;

import com.ofg.attendance.model.entity.Course;
import com.ofg.attendance.model.entity.Student;
import com.ofg.attendance.model.request.StudentCreateRequest;
import com.ofg.attendance.model.response.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StudentService {
    Page<StudentResponse> getAllStudents(Pageable pageable);

    Page<StudentResponse> getAllStudentsByCourseId(UUID courseId, Pageable pageable);

    StudentResponse getStudentById(UUID studentId);

    Student getStudentEntityByUserId(UUID userId);

    StudentResponse addStudent(StudentCreateRequest studentCreateRequest);

    void addStudentToCourse(Student student, Course course);

    void deleteStudent(UUID studentId);
}
