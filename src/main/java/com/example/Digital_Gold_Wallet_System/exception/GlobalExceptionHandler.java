package com.example.Digital_Gold_Wallet_System.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Handles all Custom Exception
    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleAppException(AppException ex){
        return new ResponseEntity<>(ex.getMessage(),ex.getStatus());
    }

    //Handles @Valid validation features
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex){
        String message=ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e->e.getField()+ ": "+e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    //Handles DB constraint violations
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrity(DataIntegrityViolationException ex){
        return new ResponseEntity<>("Data Integrity violation: "+ex.getMostSpecificCause().getMessage(),HttpStatus.CONFLICT);
    }

    //Handles missing @RequestParam
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParam(MissingServletRequestParameterException ex){
        return new ResponseEntity<>("Missing parameter : "+ex.getParameterName(),HttpStatus.BAD_REQUEST);
    }

    //Handles wrong type in path/query param
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex){
        return new ResponseEntity<>("Invalid value for parameter: "+ex.getName(),HttpStatus.BAD_REQUEST);
    }

    //Handles all remaining unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex){
        return new ResponseEntity<>("An unexpected error occurred: "+ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

}
