package com.ofg.attendance.service.concretes;

import com.ofg.attendance.exception.authentication.UnauthorizedException;
import com.ofg.attendance.exception.general.NotFoundException;
import com.ofg.attendance.model.entity.Course;
import com.ofg.attendance.model.entity.Instructor;
import com.ofg.attendance.model.entity.Student;
import com.ofg.attendance.model.request.AssignStudentToCourseRequest;
import com.ofg.attendance.model.request.CourseCreateRequest;
import com.ofg.attendance.model.request.CourseUpdateRequest;
import com.ofg.attendance.model.response.CourseResponse;
import com.ofg.attendance.repository.CourseRepository;
import com.ofg.attendance.service.abstracts.CourseService;
import com.ofg.attendance.service.abstracts.InstructorService;
import com.ofg.attendance.service.abstracts.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final InstructorService instructorService;
    private final StudentService studentService;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             InstructorService instructorService,
                             StudentService studentService) {
        this.courseRepository = courseRepository;
        this.instructorService = instructorService;
        this.studentService = studentService;
    }

    @Override
    public Page<CourseResponse> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable)
                .map(CourseResponse::new);
    }

    @Override
    public Page<CourseResponse> getAllCoursesByStudentId(UUID userId, UUID studentId, Pageable pageable) {
        validateStudentOwnership(userId, studentId);
        return courseRepository.findAllByStudentId(studentId, pageable)
                .orElseThrow(() -> new NotFoundException(studentId))
                .map(CourseResponse::new);
    }

    private void validateStudentOwnership(UUID userId, UUID studentId) {
        Student student = studentService.getStudentEntityByUserId(userId);
        if (!Objects.equals(student.getUser().getId(), userId)) {
            throw new UnauthorizedException();
        }
    }

    @Override
    public Page<CourseResponse> getAllCoursesByInstructorId(UUID instructorId, Pageable pageable) {
        return courseRepository.findAllByInstructorId(instructorId, pageable)
                .map(CourseResponse::new);
    }

    @Override
    public CourseResponse getCourseResponseById(UUID courseId) {
        return courseRepository.findById(courseId)
                .map(CourseResponse::new)
                .orElseThrow(() -> new NotFoundException(courseId));
    }

    @Override
    public Course getCourseEntityById(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException(courseId));
    }

    @Override
    public CourseResponse addCourse(UUID userId, CourseCreateRequest createRequest) {
        Instructor instructor = instructorService.getInstructorEntityByUserId(userId);

        Course course = new Course();
        course.setInstructor(instructor);
        course.setCourseName(createRequest.courseName());
        course.setCourseCode(createRequest.courseCode());

        Course savedCourse = courseRepository.save(course);
        return new CourseResponse(savedCourse);
    }

    @Override
    public void assignStudentToCourse(AssignStudentToCourseRequest assignStudentToCourseRequest) {
        Student student = studentService.getStudentEntityByUserId(assignStudentToCourseRequest.userId());
        Course course = getCourseEntityById(assignStudentToCourseRequest.courseId());

        course.getStudents().add(student);
        courseRepository.save(course);

        studentService.addStudentToCourse(student, course);
    }

    @Override
    public CourseResponse updateCourse(UUID userId, UUID courseId, CourseUpdateRequest CourseUpdateRequest) {
        Course existingCourse = getAndValidateCourseOwnership(userId, courseId);
        existingCourse.setCourseName(CourseUpdateRequest.courseName());
        Course updatedCourse = courseRepository.save(existingCourse);
        return new CourseResponse(updatedCourse);
    }

    @Override
    public void deleteCourse(UUID userId, UUID courseId) {
        getAndValidateCourseOwnership(userId, courseId);
        courseRepository.deleteById(courseId);
    }

    private Course getAndValidateCourseOwnership(UUID userId, UUID courseId) {
        Instructor instructor = instructorService.getInstructorEntityByUserId(userId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException(courseId));
        if (!Objects.equals(course.getInstructor(), instructor)) {
            throw new UnauthorizedException();
        }
        return course;
    }
}
