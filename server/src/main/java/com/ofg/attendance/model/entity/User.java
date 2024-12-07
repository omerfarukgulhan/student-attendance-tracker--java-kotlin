package com.ofg.attendance.model.entity;

import com.ofg.attendance.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Size(min = 3, max = 255)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 255)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    @Size(min = 10, max = 15)
    private String phoneNumber;

    @Column(nullable = false)
    @Size(min = 8, max = 255)
    private String password;

    @Column(nullable = false)
    private boolean active = false;

    @Column(name = "activation_token", unique = true)
    private String activationToken;

    @Column(name = "profile_image", nullable = false)
    private String profileImage = "default.png";

    @Column(name = "password_reset_token", unique = true)
    private String passwordResetToken;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
