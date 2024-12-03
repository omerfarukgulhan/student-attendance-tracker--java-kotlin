package com.ofg.attendance.security.token;

import com.ofg.attendance.model.entity.User;
import com.ofg.attendance.model.request.SignInCredentials;

import java.util.Optional;

public interface TokenService {
    Token createToken(User user, SignInCredentials signInCredentials);

    Optional<User> verifyToken(String authorizationHeader);
}