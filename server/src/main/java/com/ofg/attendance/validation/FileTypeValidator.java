package com.ofg.attendance.validation;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileTypeValidator implements ConstraintValidator<FileType, MultipartFile> {
    private String[] fileTypes;

    @Override
    public void initialize(FileType fileType) {
        this.fileTypes = fileType.types();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) return true;
        String contentType = file.getContentType();
        for (String validType : fileTypes) {
            if (contentType != null && contentType.contains(validType)) {
                return true;
            }
        }

        String validTypes = Arrays.stream(fileTypes).collect(Collectors.joining(", "));
        context.disableDefaultConstraintViolation();
        HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
        hibernateContext.addMessageParameter("types", validTypes);
        hibernateContext.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addConstraintViolation();

        return false;
    }
}