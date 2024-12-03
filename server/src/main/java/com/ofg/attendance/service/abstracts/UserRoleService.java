package com.ofg.attendance.service.abstracts;

import com.ofg.attendance.model.request.AssignRoleRequest;

import java.util.UUID;

public interface UserRoleService {
    void assignRoleToUser(AssignRoleRequest assignRoleRequest);

    void revokeRoleFromUser(UUID userId, String roleName);
}
