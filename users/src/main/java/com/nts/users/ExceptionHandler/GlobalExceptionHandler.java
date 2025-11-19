package com.nts.users.ExceptionHandler;


import com.nts.users.ExceptionHandler.CustomException.UserNotFoundException;
import com.nts.users.ResponseBody.APIErrorResponse;
import com.nts.users.ResponseBody.APIResponseBody;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponseBody> handleMethodArguementNotValidException(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toUnmodifiableList());

        String errorMessage = String.join(", ", errors);
        APIErrorResponse response = new APIErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", errorMessage);
        logger.warn(errorMessage);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }


    @ExceptionHandler(JwtException.class)
    public  ResponseEntity<APIResponseBody> handleJwtException(JwtException ex){
        String errors = ex.getLocalizedMessage();
        APIErrorResponse response = new APIErrorResponse(HttpStatus.UNAUTHORIZED,"User is Unauthorized: ", errors);
        logger.warn(ex.getStackTrace().toString());
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public  ResponseEntity<APIResponseBody> handleUserNotFoundException(UserNotFoundException ex){
        String errorMessage = ex.getMessage();
        APIErrorResponse response = new APIErrorResponse(HttpStatus.BAD_REQUEST, "User Not Found", errorMessage);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResponseBody> handleRuntimeException(RuntimeException ex){
        String errorMessage = ex.getMessage();
        APIErrorResponse response = new APIErrorResponse("Runtime Exception occurred: ", errorMessage);
        logger.warn(ex.getStackTrace().toString());

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponseBody> handleException(Exception ex){
        String errorMessage = ex.getMessage();
        APIErrorResponse response = new APIErrorResponse("Exception occurred: ", errorMessage);
        logger.warn(ex.getStackTrace().toString());
        logger.warn(errorMessage);
        ex.printStackTrace();
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
