package com.amanboora.parking.Exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ApiExceptionModel {
    private final String message;
    private final int httpStatusCode;
    private final String httpStatusName;
    private final ZonedDateTime timestamp;

    public ApiExceptionModel(ZonedDateTime timestamp, HttpStatus httpStatus, String message) {
        this.timestamp = timestamp;
        this.httpStatusCode = httpStatus.value();
        this.httpStatusName=httpStatus.name();
        this.message = message;
    }
}
