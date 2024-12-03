package com.ofg.attendance.service.abstracts;

import com.ofg.attendance.model.request.SignInCredentials;
import com.ofg.attendance.model.request.UserCreateRequest;
import com.ofg.attendance.model.response.AuthResponse;
import com.ofg.attendance.model.response.UserResponse;

public interface AuthService {
    AuthResponse login(SignInCredentials signInCredentials);

    UserResponse register(UserCreateRequest userCreateRequest);
}
