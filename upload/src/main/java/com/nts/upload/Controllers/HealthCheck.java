package com.nts.upload.Controllers;


import com.nts.upload.ApiResponseBody.ApiSuccessResponse;
import com.nts.upload.ApiResponseBody.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-check")
public class HealthCheck {

    @GetMapping
    public ResponseEntity<ResponseBody> healthCheck(){
        ResponseBody response = new ApiSuccessResponse<>("Server running successfully");
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

}
