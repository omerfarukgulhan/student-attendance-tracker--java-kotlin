package com.ofg.attendance.service.concretes;

import com.ofg.attendance.exception.general.NotFoundException;
import com.ofg.attendance.model.entity.Attendance;
import com.ofg.attendance.model.entity.QRCode;
import com.ofg.attendance.model.entity.Student;
import com.ofg.attendance.model.response.AttendanceResponse;
import com.ofg.attendance.repository.AttendanceRepository;
import com.ofg.attendance.service.abstracts.AttendanceService;
import com.ofg.attendance.service.abstracts.QRCodeService;
import com.ofg.attendance.service.abstracts.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final QRCodeService qrCodeService;
    private final StudentService studentService;

    @Autowired
    public AttendanceServiceImpl(AttendanceRepository attendanceRepository,
                                 QRCodeService qrCodeService,
                                 StudentService studentService) {
        this.attendanceRepository = attendanceRepository;
        this.qrCodeService = qrCodeService;
        this.studentService = studentService;
    }

    @Override
    public Page<AttendanceResponse> getAttendancesByLectureId(UUID lectureId, Pageable pageable) {
        return attendanceRepository.findByLectureId(lectureId, pageable)
                .orElseThrow(() -> new NotFoundException(lectureId))
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
