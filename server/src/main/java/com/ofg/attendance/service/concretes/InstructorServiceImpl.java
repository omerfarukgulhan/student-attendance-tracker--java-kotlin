package com.ofg.attendance.service.concretes;

import com.ofg.attendance.exception.authentication.UnauthorizedException;
import com.ofg.attendance.exception.general.NotFoundException;
import com.ofg.attendance.model.entity.Instructor;
import com.ofg.attendance.model.entity.User;
import com.ofg.attendance.model.request.AssignRoleRequest;
import com.ofg.attendance.model.request.InstructorCreateRequest;
import com.ofg.attendance.model.request.InstructorUpdateRequest;
import com.ofg.attendance.model.response.InstructorResponse;
import com.ofg.attendance.repository.InstructorRepository;
import com.ofg.attendance.service.abstracts.InstructorService;
import com.ofg.attendance.service.abstracts.UserRoleService;
import com.ofg.attendance.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class InstructorServiceImpl implements InstructorService {
    private final InstructorRepository instructorRepository;
    private final UserRoleService userRoleService;
    private final UserService userService;

    @Autowired
    public InstructorServiceImpl(InstructorRepository instructorRepository,
                                 UserRoleService userRoleService,
                                 UserService userService) {
        this.instructorRepository = instructorRepository;
        this.userRoleService = userRoleService;
        this.userService = userService;
    }

    @Override
    public Page<InstructorResponse> getAllInstructors(Pageable pageable) {
        return instructorRepository.findAll(pageable)
                .map(InstructorResponse::new);
    }

    @Override
    public InstructorResponse getInstructorById(UUID instructorId) {
        return instructorRepository.findById(instructorId)
                .map(InstructorResponse::new)
                .orElseThrow(() -> new NotFoundException(instructorId));
    }

    @Override
    public Instructor getInstructorEntityByUserId(UUID userId) {
        return instructorRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(userId));
    }

    @Override
    public InstructorResponse addInstructor(InstructorCreateRequest instructorCreateRequest) {
        User user = userService.getUserEntityById(instructorCreateRequest.userId());
        Instructor instructor = new Instructor();
        instructor.setUser(user);
        instructor.setDepartment(instructorCreateRequest.department());
        Instructor savedInstructor = instructorRepository.save(instructor);

        AssignRoleRequest assignRoleRequest = new AssignRoleRequest(user.getId(), "ROLE_INSTRUCTOR");
        userRoleService.assignRoleToUser(assignRoleRequest);

        return new InstructorResponse(savedInstructor);
    }

    @Override
    public InstructorResponse updateInstructor(UUID userId, UUID instructorId, InstructorUpdateRequest instructorUpdateRequest) {
        Instructor existingInstructor = getAndValidateInstructorOwnership(userId, instructorId);
        existingInstructor.setDepartment(instructorUpdateRequest.department());
        Instructor updatedInstructor = instructorRepository.save(existingInstructor);
        return new InstructorResponse(updatedInstructor);
    }

    @Override
    public void deleteInstructor(UUID userId, UUID instructorId) {
        getAndValidateInstructorOwnership(userId, instructorId);
        userRoleService.revokeRoleFromUser(userId, "ROLE_INSTRUCTOR");
        instructorRepository.deleteById(instructorId);
    }

    private Instructor getAndValidateInstructorOwnership(UUID userId, UUID instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new NotFoundException(instructorId));
        if (!Objects.equals(instructor.getUser().getId().toString(), userId.toString())) {
            throw new UnauthorizedException();
        }
        return instructor;
    }
}
