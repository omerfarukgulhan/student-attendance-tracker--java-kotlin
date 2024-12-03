package com.ofg.attendance.exception.authentication;

import com.ofg.attendance.core.util.message.Messages;
import org.springframework.context.i18n.LocaleContextHolder;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super(Messages.getMessageForLocale("app.msg.auth.invalid.credentials", LocaleContextHolder.getLocale()));
    }
}
