package com.nts.users.ResponseBody;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class APIErrorResponse extends APIResponseBody{
    private String errors;

    public APIErrorResponse(HttpStatus httpStatus, String message, String errors){
        super(false, httpStatus, message);
        this.errors = errors;
    }

    public APIErrorResponse(String message, String errors){
        super(false, HttpStatus.INTERNAL_SERVER_ERROR, message);
        this.errors = errors;
    }

}