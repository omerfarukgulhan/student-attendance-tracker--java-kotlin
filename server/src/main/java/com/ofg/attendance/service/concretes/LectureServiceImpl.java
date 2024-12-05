package com.ofg.attendance.service.concretes;

import com.ofg.attendance.exception.authentication.UnauthorizedException;
import com.ofg.attendance.exception.general.NotFoundException;
import com.ofg.attendance.model.entity.Course;
import com.ofg.attendance.model.entity.Instructor;
import com.ofg.attendance.model.entity.Lecture;
import com.ofg.attendance.model.entity.QRCode;
import com.ofg.attendance.model.request.LectureCreateRequest;
import com.ofg.attendance.model.request.LectureUpdateRequest;
import com.ofg.attendance.model.response.LectureResponse;
import com.ofg.attendance.repository.LectureRepository;
import com.ofg.attendance.service.abstracts.CourseService;
import com.ofg.attendance.service.abstracts.InstructorService;
import com.ofg.attendance.service.abstracts.LectureService;
import com.ofg.attendance.service.abstracts.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class LectureServiceImpl implements LectureService {
    private final LectureRepository lectureRepository;
    private final InstructorService instructorService;
    private final CourseService courseService;
    private final QRCodeService qrCodeService;

    @Autowired
    public LectureServiceImpl(LectureRepository lectureRepository,
                              InstructorService instructorService,
                              CourseService courseService,
                              QRCodeService qrCodeService) {
        this.lectureRepository = lectureRepository;
        this.instructorService = instructorService;
        this.courseService = courseService;
        this.qrCodeService = qrCodeService;
    }

    @Override
    public Page<LectureResponse> getAllLecturesByCourseId(UUID courseId, Pageable pageable) {
        return lectureRepository.findByCourseId(courseId, pageable)
                .orElseThrow(() -> new NotFoundException(courseId))
                .map(LectureResponse::new);
    }

    @Override
    public LectureResponse getLectureById(UUID lectureId) {
        return lectureRepository.findById(lectureId)
                .map(LectureResponse::new)
                .orElseThrow(() -> new NotFoundException(lectureId));
    }

    @Override
    public LectureResponse addLecture(UUID userId, LectureCreateRequest lectureCreateRequest) {
        Course course = validateCourseOwnership(userId, lectureCreateRequest.courseId());
        Lecture lecture = createLecture(course, lectureCreateRequest);
        Lecture savedLecture = lectureRepository.save(lecture);
        QRCode qrCode = generateAndSaveQRCodeForLecture(savedLecture);
        associateQRCodeWithLecture(lecture, qrCode);
        Lecture finalLecture = lectureRepository.save(lecture);
        return new LectureResponse(finalLecture);
    }

    private Course validateCourseOwnership(UUID userId, UUID courseId) {
        Instructor instructor = instructorService.getInstructorEntityByUserId(userId);
        Course course = courseService.getCourseEntityById(courseId);
        if (!Objects.equals(course.getInstructor().getId(), instructor.getId())) {
            throw new UnauthorizedException();
        }
        return course;
    }

    private Lecture createLecture(Course course, LectureCreateRequest request) {
        Lecture lecture = new Lecture();
        lecture.setCourse(course);
        lecture.setStartTime(request.startTime());
        lecture.setEndTime(request.endTime());
        return lecture;
    }

    private QRCode generateAndSaveQRCodeForLecture(Lecture lecture) {
        return qrCodeService.generateQRCodeForLecture(lecture);
    }

    private void associateQRCodeWithLecture(Lecture lecture, QRCode qrCode) {
        lecture.setQrCode(qrCode);
    }

    @Override
    public LectureResponse updateLecture(UUID userId, UUID lectureId, LectureUpdateRequest lectureUpdateRequest) {
        Lecture existingLecture = getAndValidateLectureOwnership(userId, lectureId);
        updateLectureDetails(existingLecture, lectureUpdateRequest);
        Lecture updatedLecture = lectureRepository.save(existingLecture);
        return new LectureResponse(updatedLecture);
    }

    private void updateLectureDetails(Lecture lecture, LectureUpdateRequest request) {
        lecture.setStartTime(request.startTime());
        lecture.setEndTime(request.endTime());
    }

    @Override
    public void deleteLecture(UUID userId, UUID lectureId) {
        Lecture lecture = getAndValidateLectureOwnership(userId, lectureId);
        qrCodeService.deleteQRCodeImage(lecture.getQrCode());
        lectureRepository.deleteById(lectureId);
    }


    private Lecture getAndValidateLectureOwnership(UUID userId, UUID lectureId) {
        Instructor instructor = instructorService.getInstructorEntityByUserId(userId);
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new NotFoundException(lectureId));
        if (!Objects.equals(lecture.getCourse().getInstructor(), instructor)) {
            throw new UnauthorizedException();
        }
        return lecture;
    }
}
