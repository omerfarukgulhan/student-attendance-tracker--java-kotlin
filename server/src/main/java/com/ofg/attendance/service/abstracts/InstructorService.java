package com.ofg.attendance.service.abstracts;

import com.ofg.attendance.model.request.InstructorCreateRequest;
import com.ofg.attendance.model.request.InstructorUpdateRequest;
import com.ofg.attendance.model.response.InstructorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface InstructorService {
    Page<InstructorResponse> getAllInstructors(Pageable pageable);

    InstructorResponse getInstructorById(UUID instructorId);

    InstructorResponse addInstructor(InstructorCreateRequest instructorCreateRequest);

    InstructorResponse updateInstructor(UUID userId, UUID instructorId, InstructorUpdateRequest instructorUpdateRequest);

    void deleteInstructor(UUID userId, UUID instructorId);
}
