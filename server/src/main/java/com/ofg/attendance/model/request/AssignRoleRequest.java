package com.ofg.attendance.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AssignRoleRequest(
        UUID userId,
        @NotBlank(message = "{app.constraint.role-name.not-blank}")
        @Size(min = 3, max = 100, message = "{app.constraint.role-name.size}")
        String roleName
) {

}