package com.project.tfms.security;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;




public class CustomPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public String encode(CharSequence rawPassword) {
        validatePassword(rawPassword.toString());
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    private void validatePassword(String password) {
        PasswordValidator passwordValidator = new PasswordValidator(password);
        Set<ConstraintViolation<PasswordValidator>> violations = validator.validate(passwordValidator);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<PasswordValidator> violation : violations) {
                sb.append(violation.getMessage()).append(" ");
            }
            throw new ConstraintViolationException(sb.toString().trim(), violations);
        }
    }

    public static class PasswordValidator {
        @NotNull
        @Size(min = 8, message = "Password must be at least 8 characters long")
        private final String password;

        public PasswordValidator(String password) {
            this.password = password;
        }
    }
}
