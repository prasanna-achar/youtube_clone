package com.nts.users.ResponseBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Data
@AllArgsConstructor

public  final class APIFailResponse extends APIResponseBody{

    public APIFailResponse(HttpStatus httpStatus, String message){
        super(false, httpStatus, message);
    }
    public APIFailResponse(String message){
        super(false, HttpStatus.NOT_ACCEPTABLE, message);
    }
}