package com.nts.upload.ApiResponseBody;

import org.springframework.http.HttpStatus;

public class ApiSuccessResponse<type> extends ResponseBody {
    private type data;
    public ApiSuccessResponse(){
        super(true, HttpStatus.OK,"OK");
    }
    public ApiSuccessResponse(HttpStatus httpStatus, String message, type data){
        super(true, httpStatus, message);
        this.data = data;
    }
    public ApiSuccessResponse(boolean success, HttpStatus httpStatus, String message, type data){
        super(success, httpStatus,message);
        this.data = data;
    }
    public ApiSuccessResponse(boolean success, HttpStatus httpStatus,int httpStatusCode, String message, type data){
        super(success, httpStatus,httpStatusCode,message);
        this.data = data;
    }

    public ApiSuccessResponse(String message){
        super(true, HttpStatus.OK, message);
        this.data = null;
    }


    public type getData() {
        return data;
    }

    public void setData(type data) {
        this.data = data;
    }
}
