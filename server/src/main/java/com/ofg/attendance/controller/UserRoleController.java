package com.ofg.attendance.controller;

import com.ofg.attendance.core.util.response.ResponseUtil;
import com.ofg.attendance.core.util.results.ApiResponse;
import com.ofg.attendance.model.request.AssignRoleRequest;
import com.ofg.attendance.service.abstracts.UserRoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user-roles")
public class UserRoleController {
    private final UserRoleService userRoleService;

    private static final String USER_ROLE_ASSIGNED_SUCCESS = "app.msg.user.roles.assign.success";
    private static final String USER_ROLE_REVOKED_SUCCESS = "app.msg.user.roles.revoke.success";

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping("/assign")
    public ResponseEntity<ApiResponse> assignRoleToUser(@Valid @RequestBody AssignRoleRequest assignRoleRequest) {
        userRoleService.assignRoleToUser(assignRoleRequest);
        return ResponseUtil.createApiResponse(USER_ROLE_ASSIGNED_SUCCESS, HttpStatus.OK);
    }

    @DeleteMapping("/revoke")
    public ResponseEntity<ApiResponse> revokeRoleFromUser(
            @RequestParam UUID userId,
            @RequestParam String roleName) {
        userRoleService.revokeRoleFromUser(userId, roleName);
        return ResponseUtil.createApiResponse(USER_ROLE_REVOKED_SUCCESS, HttpStatus.NO_CONTENT);
    }
}