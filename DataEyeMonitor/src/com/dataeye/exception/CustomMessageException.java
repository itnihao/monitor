package com.dataeye.exception;



public class CustomMessageException extends AbstractDataEyeException{

    public CustomMessageException(int statusCode, String message) {
        super(statusCode, message);
    }
}
