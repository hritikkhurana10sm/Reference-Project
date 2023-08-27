package com.amanboora.parking.Exception;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler{

    @ExceptionHandler({ResourceNotFound.class})
    public ResponseEntity<Object> handleAPiNoContentFoundException(ResourceNotFound ex){
        ApiExceptionModel apiExceptionModel=new ApiExceptionModel(
                ZonedDateTime.now(),
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        return new ResponseEntity<>(apiExceptionModel,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DataInvalidationException.class})
    public ResponseEntity<Object> handleDataValidationException(DataInvalidationException ex){
        ApiExceptionModel apiExceptionModel=new ApiExceptionModel(
                ZonedDateTime.now(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        return new ResponseEntity<>(apiExceptionModel,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ParkingException.class})
    public ResponseEntity<Object> handleParkingException(ParkingException ex){
        ApiExceptionModel apiExceptionModel=new ApiExceptionModel(
                ZonedDateTime.now(),
                ex.getHttpStatus(),
                ex.getMessage()
        );
        return new ResponseEntity<>(apiExceptionModel, ex.getHttpStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        ApiExceptionModel apiExceptionModel=new ApiExceptionModel(
                ZonedDateTime.now(),
                HttpStatus.BAD_REQUEST,
                "Input filled is missing!  "+ex.getMessage()
        );
        return new ResponseEntity<>(apiExceptionModel,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({OptimisticLockingFailureException.class})
    public ResponseEntity<Object> handleOptimisticLockingFailureException(OptimisticLockingFailureException ex){
        ApiExceptionModel apiExceptionModel=new ApiExceptionModel(
                ZonedDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMostSpecificCause().getMessage()
        );
        return new ResponseEntity<>(apiExceptionModel,HttpStatus.BAD_REQUEST);
    }
}
