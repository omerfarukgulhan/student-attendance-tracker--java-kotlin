package com.ofg.attendance.service.concretes;

import com.ofg.attendance.exception.general.NotFoundException;
import com.ofg.attendance.model.entity.Student;
import com.ofg.attendance.model.entity.User;
import com.ofg.attendance.model.request.AssignRoleRequest;
import com.ofg.attendance.model.request.StudentCreateRequest;
import com.ofg.attendance.model.response.StudentResponse;
import com.ofg.attendance.repository.StudentRepository;
import com.ofg.attendance.service.abstracts.StudentService;
import com.ofg.attendance.service.abstracts.UserRoleService;
import com.ofg.attendance.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final UserService userService;
    private final UserRoleService userRoleService;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              UserService userService,
                              UserRoleService userRoleService) {
        this.studentRepository = studentRepository;
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @Override
    public Page<StudentResponse> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable)
                .map(StudentResponse::new);
    }

    @Override
    public Page<StudentResponse> getAllStudentsByCourseId(UUID courseId, Pageable pageable) {
        return null;
    }

    @Override
    public StudentResponse getStudentById(UUID studentId) {
        return studentRepository.findById(studentId)
                .map(StudentResponse::new)
                .orElseThrow(() -> new NotFoundException(studentId));
    }

    @Override
    public StudentResponse addStudent(StudentCreateRequest studentCreateRequest) {
        User user = userService.getUserEntityById(studentCreateRequest.userId());
        Student student = new Student();
        student.setUser(user);
        student.setEnrollmentNumber(generateEnrollmentNumber());
        Student savedStudent = studentRepository.save(student);
        AssignRoleRequest assignRoleRequest = new AssignRoleRequest(studentCreateRequest.userId(), "ROLE_STUDENT");
        userRoleService.assignRoleToUser(assignRoleRequest);
        return new StudentResponse(savedStudent);
    }

    public static String generateEnrollmentNumber() {
        String prefix = "24";
        Random random = new Random();
        StringBuilder randomDigits = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            randomDigits.append(random.nextInt(10));
        }
        return prefix + randomDigits;
    }

    @Override
    public void deleteStudent(UUID studentId) {
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException(studentId));
        userRoleService.revokeRoleFromUser(existingStudent.getUser().getId(), "ROLE_STUDENT");
        studentRepository.deleteById(studentId);
    }
}
