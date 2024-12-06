package com.ofg.attendance.service.concretes;

import com.ofg.attendance.exception.authentication.UnauthorizedException;
import com.ofg.attendance.exception.general.NotFoundException;
import com.ofg.attendance.model.entity.*;
import com.ofg.attendance.model.response.AttendanceResponse;
import com.ofg.attendance.repository.AttendanceRepository;
import com.ofg.attendance.service.abstracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final QRCodeService qrCodeService;
    private final StudentService studentService;
    private final LectureService lectureService;
    private final InstructorService instructorService;
    private final CourseService courseService;

    @Autowired
    public AttendanceServiceImpl(AttendanceRepository attendanceRepository,
                                 QRCodeService qrCodeService,
                                 StudentService studentService,
                                 LectureService lectureService,
                                 InstructorService instructorService,
                                 CourseService courseService) {
        this.attendanceRepository = attendanceRepository;
        this.qrCodeService = qrCodeService;
        this.studentService = studentService;
        this.lectureService = lectureService;
        this.instructorService = instructorService;
        this.courseService = courseService;
    }

    @Override
    public Page<AttendanceResponse> getAttendancesByLectureId(UUID userId, UUID lectureId, Pageable pageable) {
        validateLectureOwnership(userId, lectureId);
        return attendanceRepository.findByLectureId(lectureId, pageable)
                .orElseThrow(() -> new NotFoundException(lectureId))
                .map(AttendanceResponse::new);
    }

    private void validateLectureOwnership(UUID userId, UUID lectureId) {
        Instructor instructor = instructorService.getInstructorEntityByUserId(userId);
        Lecture lecture = lectureService.getLectureEntityById(lectureId);
        if (!Objects.equals(lecture.getCourse().getInstructor(), instructor)) {
            throw new UnauthorizedException();
        }
    }

    @Override
    public Page<AttendanceResponse> getAllAttendancesByCourseId(UUID userId, UUID courseId, Pageable pageable) {
        validateCourseOwnership(userId, courseId);
        return attendanceRepository.findAllByCourseId(courseId, pageable)
                .orElseThrow(() -> new NotFoundException(courseId))
                .map(AttendanceResponse::new);
    }

    private void validateCourseOwnership(UUID userId, UUID courseId) {
        Instructor instructor = instructorService.getInstructorEntityByUserId(userId);
        Course course = courseService.getCourseEntityById(courseId);
        if (!Objects.equals(course.getInstructor(), instructor)) {
            throw new UnauthorizedException();
        }
    }

    @Override
    public Page<AttendanceResponse> getAllAttendancesByStudentId(UUID userId, Pageable pageable) {
        return attendanceRepository.findByStudentUserId(userId, pageable)
                .orElseThrow(() -> new NotFoundException(userId))
                .map(AttendanceResponse::new);
    }

    @Override
    public AttendanceResponse getAttendanceById(UUID attendanceId) {
        return attendanceRepository.findById(attendanceId)
                .map(AttendanceResponse::new)
                .orElseThrow(() -> new NotFoundException(attendanceId));
    }

    @Override
    public AttendanceResponse markAttendance(UUID userId, String qrContent) {
        QRCode qrCode = qrCodeService.getQRCodeByContent(qrContent);
        Student student = studentService.getStudentEntityByUserId(userId);
        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setLecture(qrCode.getLecture());

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return new AttendanceResponse(savedAttendance);
    }

    @Override
    public boolean hasStudentMarkedAttendance(UUID userId, UUID lectureId) {
        Student student = studentService.getStudentEntityByUserId(userId);
        return attendanceRepository.existsByStudentIdAndLectureId(student.getId(), lectureId);
    }
}
