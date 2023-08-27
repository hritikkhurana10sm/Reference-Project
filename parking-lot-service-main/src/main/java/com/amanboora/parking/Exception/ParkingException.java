package com.amanboora.parking.Exception;

import org.springframework.http.HttpStatus;

public class ParkingException extends RuntimeException {

    private String message;
    private final HttpStatus httpStatus;

    public ParkingException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
