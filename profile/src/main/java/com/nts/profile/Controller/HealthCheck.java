package com.nts.profile.Controller;


import com.nts.profile.ResponseBody.APIResponseBody;
import com.nts.profile.ResponseBody.APISuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/profile/health")
@RestController
public class HealthCheck {

    @GetMapping
    public ResponseEntity<APIResponseBody> healthCheck(@RequestHeader("X-User-Id") String userId){
        APISuccessResponse<String> response = new APISuccessResponse("Server running successfully", userId);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
