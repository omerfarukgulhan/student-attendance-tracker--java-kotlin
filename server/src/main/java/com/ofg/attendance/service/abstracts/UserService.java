package com.ofg.attendance.service.abstracts;

import com.ofg.attendance.model.entity.User;
import com.ofg.attendance.model.request.*;
import com.ofg.attendance.model.response.UserResponse;
import com.ofg.attendance.model.response.UserResponseWithoutRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    Page<UserResponseWithoutRoles> getAllUsers(Pageable pageable);

    UserResponse getUserResponseById(UUID userId);

    User getUserEntityById(UUID userId);

    User getUserByEmail(String email);

    UserResponse addUser(UserCreateRequest userCreateRequest);

    UserResponse updateUser(UUID userId, UserUpdateRequest userUpdateRequest);

    UserResponse activateUser(String token);

    void updatePassword(UUID userId, UserPasswordUpdateRequest userPasswordUpdateRequest);

    void resetPassword(UserPasswordResetRequest userPasswordResetRequest);

    void setPassword(String token, UserPasswordSetRequest userPasswordSetRequest);

    void deleteUser(UUID userId);
}