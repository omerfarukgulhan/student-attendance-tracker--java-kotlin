package com.ofg.attendance.exception.authentication;

import com.ofg.attendance.core.util.message.Messages;
import org.springframework.context.i18n.LocaleContextHolder;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super(Messages.getMessageForLocale("app.msg.auth.unauthorized", LocaleContextHolder.getLocale()));
    }
}
