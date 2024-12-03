package com.ofg.attendance.controller;

import com.ofg.attendance.core.util.response.ResponseUtil;
import com.ofg.attendance.core.util.results.ApiDataResponse;
import com.ofg.attendance.core.util.results.ApiResponse;
import com.ofg.attendance.model.request.InstructorCreateRequest;
import com.ofg.attendance.model.request.InstructorUpdateRequest;
import com.ofg.attendance.model.response.InstructorResponse;
import com.ofg.attendance.security.CurrentUser;
import com.ofg.attendance.service.abstracts.InstructorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/instructors")
public class InstructorController {
    private final InstructorService instructorService;

    private static final String INSTRUCTORS_FETCH_SUCCESS = "app.msg.instructors.fetch.success";
    private static final String INSTRUCTOR_FETCH_SUCCESS = "app.msg.instructor.fetch.success";
    private static final String INSTRUCTOR_CREATE_SUCCESS = "app.msg.instructor.create.success";
    private static final String INSTRUCTOR_UPDATE_SUCCESS = "app.msg.instructor.update.success";
    private static final String INSTRUCTOR_DELETE_SUCCESS = "app.msg.instructor.delete.success";

    @Autowired
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping
    public ResponseEntity<ApiDataResponse<Page<InstructorResponse>>> getAllInstructors(Pageable pageable) {
        Page<InstructorResponse> instructors = instructorService.getAllInstructors(pageable);
        return ResponseUtil.createApiDataResponse(instructors, INSTRUCTORS_FETCH_SUCCESS, HttpStatus.OK);
    }

    @GetMapping("/{instructorId}")
    public ResponseEntity<ApiDataResponse<InstructorResponse>> getInstructorById(@PathVariable UUID instructorId) {
        InstructorResponse instructor = instructorService.getInstructorById(instructorId);
        return ResponseUtil.createApiDataResponse(instructor, INSTRUCTOR_FETCH_SUCCESS, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiDataResponse<InstructorResponse>> createInstructor(@Valid @RequestBody InstructorCreateRequest instructorCreateRequest) {
        InstructorResponse instructor = instructorService.addInstructor(instructorCreateRequest);
        return ResponseUtil.createApiDataResponse(instructor, INSTRUCTOR_CREATE_SUCCESS, HttpStatus.CREATED);
    }

    @PutMapping("/{instructorId}")
    public ResponseEntity<ApiDataResponse<InstructorResponse>> updateInstructor(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable UUID instructorId,
            @Valid @RequestBody InstructorUpdateRequest instructorUpdateRequest) {
        InstructorResponse instructor = instructorService.updateInstructor(currentUser.getId(), instructorId, instructorUpdateRequest);
        return ResponseUtil.createApiDataResponse(instructor, INSTRUCTOR_UPDATE_SUCCESS, HttpStatus.OK);
    }

    @DeleteMapping("/{instructorId}")
    public ResponseEntity<ApiResponse> deleteInstructor(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable UUID instructorId) {
        instructorService.deleteInstructor(currentUser.getId(), instructorId);
        return ResponseUtil.createApiResponse(INSTRUCTOR_DELETE_SUCCESS, HttpStatus.NO_CONTENT);
    }
}
