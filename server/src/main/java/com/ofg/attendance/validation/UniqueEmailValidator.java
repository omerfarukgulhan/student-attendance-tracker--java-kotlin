package com.ofg.attendance.validation;

import com.ofg.attendance.model.entity.User;
import com.ofg.attendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserRepository userRepository;

    @Autowired
    public UniqueEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        User user = userRepository.findByEmail(value).orElse(null);
        return user == null;
    }
}