package com.ofg.attendance.model.response;

import com.ofg.attendance.model.entity.User;

import java.util.UUID;

public record UserResponseWithoutRoles(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String profileImage
) {
    public UserResponseWithoutRoles(User user) {
        this(user.getId(), user.getFirstName(),
                user.getLastName(), user.getEmail(),
                user.getProfileImage());
    }
}
