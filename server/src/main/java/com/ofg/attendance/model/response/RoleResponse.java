package com.ofg.attendance.model.response;

import com.ofg.attendance.model.entity.Role;

import java.util.UUID;

public record RoleResponse(UUID id, String name) {
    public RoleResponse(Role role) {
        this(role.getId(), role.getName());
    }
}
