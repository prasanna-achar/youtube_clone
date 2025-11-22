package com.nts.users.Controller;

import com.nts.users.ExceptionHandler.CustomException.UserNotFoundException;
import com.nts.users.ResponseBody.APIResponseBody;
import com.nts.users.DTO.RequestSchema.*;
import com.nts.users.Service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<APIResponseBody> register(@RequestBody SignupUser body){
        APIResponseBody response = authService.register(body);
        return new ResponseEntity<APIResponseBody>(response, response.getHttpStatus());
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponseBody> login(@RequestBody LoginUser body){
        APIResponseBody response = authService.login(body);
        return new ResponseEntity<APIResponseBody>(response, response.getHttpStatus());
    }
    @PostMapping("/verify/{token}")
    public ResponseEntity<APIResponseBody> verify(@PathVariable String token ,@RequestBody GetOTPSchema body, HttpServletResponse res){
        APIResponseBody response = authService.verifyUser(token,body.getOTP(), res);
        return new ResponseEntity<APIResponseBody>(response, response.getHttpStatus());
    }
    @GetMapping("/me")
    public ResponseEntity<APIResponseBody> getMe(@CookieValue(value = "jwt", required = false) String token){
        APIResponseBody response = authService.getMe(token);
        return new ResponseEntity<APIResponseBody>(response, response.getHttpStatus());
    }

    @PostMapping("/resend-otp/{token}")
    public ResponseEntity<APIResponseBody> resendOTP(@PathVariable String token){

        APIResponseBody response = authService.resendOTP(token);
        return  new ResponseEntity<APIResponseBody>(response, response.getHttpStatus());
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<APIResponseBody> forgotPassword(@RequestBody ForgotPasswordSchema body){
        APIResponseBody response = authService.forgotPasswordOrChangePassword(body.getEmail());
        return  new ResponseEntity<APIResponseBody>(response, response.getHttpStatus());
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<APIResponseBody> resetPassword(@PathVariable String token,@RequestBody ResetPasswordSchema body){
        APIResponseBody response = authService.resetPassword(token, body.getPassword());
        return  new ResponseEntity<APIResponseBody>(response, response.getHttpStatus());
    }



    // TODO: Change Password
    // TODO: OAuth Important
}
