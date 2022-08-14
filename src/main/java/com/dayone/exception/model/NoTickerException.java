package com.dayone.exception.model;

import org.springframework.http.HttpStatus;

public class NoTickerException extends AbstractException{
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "Ticker가 존재하지 않습니다.";
    }
}
