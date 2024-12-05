package com.ofg.attendance.model.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StudentCreateRequest(
        @NotNull(message = "{app.constraint.user-id.not-null}")
        UUID userId
) {

}