package com.nts.upload.ApiResponseBody;

import org.springframework.http.HttpStatus;

public class ApiFailResponse extends ResponseBody{

    public ApiFailResponse(HttpStatus httpStatus, String message){
        super(false, httpStatus, message);
    }
}
