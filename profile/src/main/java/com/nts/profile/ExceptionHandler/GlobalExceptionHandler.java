package com.nts.profile.ExceptionHandler;


import com.nts.profile.ResponseBody.APIErrorResponse;
import com.nts.profile.ResponseBody.APIResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponseBody> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::toString)
                .collect(Collectors.toUnmodifiableList());

        String errorMessage = String.join(", ", errors);
        APIErrorResponse response = new APIErrorResponse(HttpStatus.BAD_REQUEST, "Pass correct arguments",errorMessage);

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResponseBody> handleRuntimeException(RuntimeException ex){
        List<String> errors = Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toUnmodifiableList());

        for(String err : errors){
            System.out.println(err);
        }
        String errorMsg = String.join(", ", errors);
        APIErrorResponse response = new APIErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Runtime Error has been occurred", errorMsg);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponseBody> handleGeneralException(Exception ex){
        List<String> errors = Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toUnmodifiableList());

        String errorMsg = String.join(", ", errors);
        APIErrorResponse response = new APIErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Runtime Error has been occurred", errorMsg);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

}
