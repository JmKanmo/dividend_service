package com.dayone.exception.model;

public class ScrapException extends Exception {
    public ScrapException(String message) {
        super("[ScrapException] scrap exception occurred !!, cause => " + message);
    }
}
