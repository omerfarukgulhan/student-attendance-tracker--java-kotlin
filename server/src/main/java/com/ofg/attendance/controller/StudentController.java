package com.ofg.attendance.controller;

import com.ofg.attendance.core.util.response.ResponseUtil;
import com.ofg.attendance.core.util.results.ApiDataResponse;
import com.ofg.attendance.core.util.results.ApiResponse;
import com.ofg.attendance.model.request.StudentCreateRequest;
import com.ofg.attendance.model.response.StudentResponse;
import com.ofg.attendance.service.abstracts.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    private static final String STUDENTS_FETCH_SUCCESS = "app.msg.students.fetch.success";
    private static final String STUDENT_FETCH_SUCCESS = "app.msg.student.fetch.success";
    private static final String STUDENT_CREATE_SUCCESS = "app.msg.student.create.success";
    private static final String STUDENT_DELETE_SUCCESS = "app.msg.student.delete.success";

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<ApiDataResponse<Page<StudentResponse>>> getAllStudents(Pageable pageable) {
        Page<StudentResponse> students = studentService.getAllStudents(pageable);
        return ResponseUtil.createApiDataResponse(students, STUDENTS_FETCH_SUCCESS, HttpStatus.OK);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<ApiDataResponse<StudentResponse>> getStudentById(@PathVariable UUID studentId) {
        StudentResponse student = studentService.getStudentById(studentId);
        return ResponseUtil.createApiDataResponse(student, STUDENT_FETCH_SUCCESS, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiDataResponse<StudentResponse>> createStudent(
            @Valid @RequestBody StudentCreateRequest studentCreateRequest) {
        StudentResponse student = studentService.addStudent(studentCreateRequest);
        return ResponseUtil.createApiDataResponse(student, STUDENT_CREATE_SUCCESS, HttpStatus.CREATED);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable UUID studentId) {
        studentService.deleteStudent(studentId);
        return ResponseUtil.createApiResponse(STUDENT_DELETE_SUCCESS, HttpStatus.NO_CONTENT);
    }
}
