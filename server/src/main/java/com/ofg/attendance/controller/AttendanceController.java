package com.ofg.attendance.controller;

import com.ofg.attendance.core.util.response.ResponseUtil;
import com.ofg.attendance.core.util.results.ApiDataResponse;
import com.ofg.attendance.model.response.AttendanceResponse;
import com.ofg.attendance.security.CurrentUser;
import com.ofg.attendance.service.abstracts.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/attendances")
public class AttendanceController {
    private final AttendanceService attendanceService;

    private static final String ATTENDANCES_FETCH_SUCCESS = "app.msg.attendances.fetch.success";
    private static final String ATTENDANCE_FETCH_SUCCESS = "app.msg.attendance.fetch.success";
    private static final String ATTENDANCE_MARKED_SUCCESS = "app.msg.attendances.mark.success";

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/lecture/{lectureId}")
    public ResponseEntity<ApiDataResponse<Page<AttendanceResponse>>> getAllAttendancesByLectureId(
            @PathVariable UUID lectureId,
            Pageable pageable) {
        Page<AttendanceResponse> attendances = attendanceService.getAttendancesByLectureId(lectureId, pageable);
        return ResponseUtil.createApiDataResponse(attendances, ATTENDANCES_FETCH_SUCCESS, HttpStatus.OK);
    }

    @GetMapping("/{attendanceId}")
    public ResponseEntity<ApiDataResponse<AttendanceResponse>> getAttendanceById(@PathVariable UUID attendanceId) {
        AttendanceResponse attendance = attendanceService.getAttendanceById(attendanceId);
        return ResponseUtil.createApiDataResponse(attendance, ATTENDANCE_FETCH_SUCCESS, HttpStatus.OK);
    }

    @PostMapping("/{qrContent}")
    public ResponseEntity<ApiDataResponse<AttendanceResponse>> markAttendance(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable String qrContent) {
        AttendanceResponse attendance = attendanceService.markAttendance(currentUser.getId(), qrContent);
        return ResponseUtil.createApiDataResponse(attendance, ATTENDANCE_MARKED_SUCCESS, HttpStatus.CREATED);
    }
}
