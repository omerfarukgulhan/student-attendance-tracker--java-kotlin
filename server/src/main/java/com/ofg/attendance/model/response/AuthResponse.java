package com.ofg.attendance.model.response;

import com.ofg.attendance.security.token.Token;

public record AuthResponse(UserResponse userResponse, Token token) {
}
