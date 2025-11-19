package com.nts.users.ResponseBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public final class APISuccessResponse<type> extends APIResponseBody {
    private  type data;

    public APISuccessResponse(boolean success, HttpStatus httpStatus, String message, type data){
        super(success,httpStatus, message );
        this.data = data;
    }

    public APISuccessResponse(HttpStatus httpStatus, String message, type data){
        super(true, httpStatus, message);
        this.data = data;
    }

    public APISuccessResponse(String message, type data){
        super(true, HttpStatus.OK, message);
        this.data = data;

    }
    public  APISuccessResponse(String message){
        super(true, HttpStatus.OK, message);
        this.data = null;
    }
}

