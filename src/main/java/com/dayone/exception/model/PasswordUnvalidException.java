package com.dayone.exception.model;

import org.springframework.http.HttpStatus;

public class PasswordUnvalidException extends AbstractException{
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "틀린 비밀번호입니다.";
    }
}
