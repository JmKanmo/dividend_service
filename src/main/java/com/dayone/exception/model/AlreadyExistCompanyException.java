package com.dayone.exception.model;

import org.springframework.http.HttpStatus;

public class AlreadyExistCompanyException extends AbstractException{
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 저장 된 회사입니다.";
    }
}
