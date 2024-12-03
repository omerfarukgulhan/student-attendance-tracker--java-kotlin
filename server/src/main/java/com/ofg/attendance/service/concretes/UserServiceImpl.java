package com.ofg.attendance.service.concretes;

import com.ofg.attendance.exception.email.ActivationNotificationException;
import com.ofg.attendance.exception.authentication.InvalidPasswordException;
import com.ofg.attendance.exception.authentication.InvalidTokenException;
import com.ofg.attendance.exception.general.NotFoundException;
import com.ofg.attendance.exception.general.NotUniqueEmailException;
import com.ofg.attendance.model.entity.Role;
import com.ofg.attendance.model.entity.User;
import com.ofg.attendance.model.request.*;
import com.ofg.attendance.model.response.UserResponse;
import com.ofg.attendance.model.response.UsersListResponse;
import com.ofg.attendance.repository.UserRepository;
import com.ofg.attendance.service.abstracts.EmailService;
import com.ofg.attendance.service.abstracts.FileService;
import com.ofg.attendance.service.abstracts.RoleService;
import com.ofg.attendance.service.abstracts.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           FileService fileService,
                           PasswordEncoder passwordEncoder,
                           EmailService emailService,
                           RoleService roleService) {
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.roleService = roleService;
    }

    @Override
    public Page<UsersListResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAllActiveUsers(pageable)
                .map(UsersListResponse::new);
    }

    @Override
    public UserResponse getUserResponseById(UUID userId) {
        return userRepository.findActiveByIdWithRoles(userId)
                .map(UserResponse::new)
                .orElseThrow(() -> new NotFoundException(userId));
    }

    @Override
    public User getUserEntityById(UUID userId) {
        return userRepository.findActiveByIdWithRoles(userId)
                .orElseThrow(() -> new NotFoundException(userId));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findActiveByEmailWithRoles(email)
                .orElseThrow(() -> new NotFoundException(email));
    }

    @Override
    @Transactional(rollbackOn = MailException.class)
    public UserResponse addUser(UserCreateRequest userCreateRequest) {
        try {
            User user = createUser(userCreateRequest);
            Role userRole = roleService.getRoleByName("ROLE_USER");
            user.getRoles().add(userRole);
            User savedUser = userRepository.saveAndFlush(user);
            emailService.sendActivationEmail(user.getEmail(), user.getActivationToken());
            return new UserResponse(savedUser);
        } catch (DataIntegrityViolationException ex) {
            throw new NotUniqueEmailException();
        } catch (MailException ex) {
            throw new ActivationNotificationException();
        }
    }

    @Override
    public UserResponse updateUser(UUID userId, UserUpdateRequest userUpdateRequest) {
        User existingUser = findUserById(userId);
        updateUserDetails(existingUser, userUpdateRequest);
        MultipartFile profileImage = userUpdateRequest.profileImage();

        if (profileImage != null && !profileImage.isEmpty()) {
            updateUserProfileImage(existingUser, profileImage);
        }

        User updatedUser = userRepository.save(existingUser);
        return new UserResponse(updatedUser);
    }

    @Override
    public void updatePassword(UUID userId, UserPasswordUpdateRequest userPasswordUpdateRequest) {
        User user = findUserById(userId);
        validateOldPassword(user, userPasswordUpdateRequest.oldPassword());
        user.setPassword(passwordEncoder.encode(userPasswordUpdateRequest.newPassword()));
        userRepository.save(user);
    }

    @Override
    public UserResponse activateUser(String token) {
        User user = userRepository.findByActivationToken(token)
                .orElseThrow(() -> new InvalidTokenException("activation"));
        user.setActive(true);
        user.setActivationToken(null);
        User activatedUser = userRepository.save(user);
        return new UserResponse(activatedUser);
    }

    @Override
    public void resetPassword(UserPasswordResetRequest userPasswordResetRequest) {
        User user = userRepository.findByEmail(userPasswordResetRequest.email())
                .orElseThrow(() -> new NotFoundException(userPasswordResetRequest.email()));
        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        userRepository.save(user);
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
    }

    @Override
    public void setPassword(String token, UserPasswordSetRequest userPasswordSetRequest) {
        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new InvalidTokenException("reset"));
        user.setPassword(passwordEncoder.encode(userPasswordSetRequest.newPassword()));
        user.setPasswordResetToken(null);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = findUserById(userId);
        deleteUserProfileImageIfExists(user);
        userRepository.delete(user);
    }

    private User createUser(UserCreateRequest userCreateRequest) {
        User user = userCreateRequest.toUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivationToken(UUID.randomUUID().toString());
        return user;
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(userId));
    }

    private void updateUserDetails(User user, UserUpdateRequest request) {
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhoneNumber(request.phoneNumber());
    }

    private void updateUserProfileImage(User user, MultipartFile file) {
        String fileName = fileService.saveFile("profile", file);
        fileService.deleteImage("profile", user.getProfileImage());
        user.setProfileImage(fileName);
    }

    private void validateOldPassword(User user, String oldPassword) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidPasswordException();
        }
    }

    private void deleteUserProfileImageIfExists(User user) {
        if (user.getProfileImage() != null) {
            fileService.deleteImage("profile", user.getProfileImage());
        }
    }
}
