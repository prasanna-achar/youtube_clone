package com.nts.users.ResponseBody;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class APIResponseBody {
    private boolean success;
    private HttpStatus httpStatus;
    private int status;
    private String message;


    public APIResponseBody(boolean success, HttpStatus httpStatus, String message){
        this.success = success;
        this.httpStatus =httpStatus;
        this.status = httpStatus.value();
        this.message = message;
    }
}
