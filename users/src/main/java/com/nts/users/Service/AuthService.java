package com.nts.users.Service;



import com.nts.users.DTO.RequestSchema.LoginUser;
import com.nts.users.DTO.RequestSchema.SignupUser;
import com.nts.users.DTO.ResponseSchema.TokenSchema;
import com.nts.users.DTO.ResponseSchema.UserResponseSchema;
import com.nts.users.ExceptionHandler.CustomException.UserNotFoundException;
import com.nts.users.Model.ResetPassword;
import com.nts.users.Model.User;
import com.nts.users.Model.VerifyAuth;
import com.nts.users.Repository.AuthRepository;
import com.nts.users.Repository.ResetPasswordRepository;
import com.nts.users.Repository.VerifyAuthRepository;
import com.nts.users.ResponseBody.APIFailResponse;
import com.nts.users.ResponseBody.APIResponseBody;
import com.nts.users.ResponseBody.APISuccessResponse;
import com.nts.users.Service.Mails.MailBodyBuilder;
import com.nts.users.Utils.BcryptUtils;
import com.nts.users.Utils.JwtUtils;
import com.nts.users.Utils.TokenUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    MailService mailService;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    VerifyAuthRepository verifyAuthRepository;

    @Autowired
    ResetPasswordRepository resetPasswordRepository;

    @Autowired
    JwtUtils jwtUtils;


    public APIResponseBody login(LoginUser body){
        if(body.getEmail().isEmpty() || ! body.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")){
            return new APIFailResponse(HttpStatus.NOT_ACCEPTABLE, "Not a valid Email");
        }
        if(!authRepository.existsByEmail(body.getEmail())){
            return new APIFailResponse(HttpStatus.NOT_FOUND, "User with this email not found");
        }

        User user = authRepository.findByEmail(body.getEmail());

        if(!BcryptUtils.checkHash(body.getPassword(), user.getPassword())){
            return new APIFailResponse(HttpStatus.NOT_FOUND, "Invalid Credentials..");

        }
        String token = TokenUtils.generateToken(16);
        String oneTimePassword = TokenUtils.getOneTimePassword();

        VerifyAuth verifyAuth;

        if(verifyAuthRepository.existsByUser_Email(body.getEmail())) {
            verifyAuth = verifyAuthRepository.findByEmail(body.getEmail());
        }else{
            verifyAuth = new VerifyAuth();
        }
        verifyAuth.setToken(token);
        verifyAuth.setPassword(BcryptUtils.hash(oneTimePassword));
        verifyAuth.setUser(user);
        verifyAuth.setExpiryTime(LocalDateTime.now().plusMinutes(20));
        verifyAuthRepository.save(verifyAuth);


        mailService.sendMail(user.getEmail(), "Verify your email", MailBodyBuilder.buildVerificationEmail(user.getUsername(), oneTimePassword));

        return new APISuccessResponse<>(true, HttpStatus.OK, "User created successfully, Verify to continue",new TokenSchema(token));

    }
    public APIResponseBody register(SignupUser body){


        // checking email is empty
        if(body.getEmail().isEmpty() || ! body.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")){
            return new APIFailResponse(HttpStatus.NOT_ACCEPTABLE, "Not a valid Email");
        }

        // checking password secure
        if(body.getPassword().isEmpty() ||
                !body.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$")) {
            return new APIFailResponse(HttpStatus.NOT_ACCEPTABLE,
                    "Password must be at least 8 characters long, " +
                            "contain uppercase, lowercase, digit, and special character");
        }


        // checking user name given or already
        if(body.getUsername().isEmpty() ||
                !body.getUsername().matches("^[a-z][a-z0-9._]{2,19}$")) {
            return new APIFailResponse(HttpStatus.NOT_ACCEPTABLE,
                    "Username must start with a letter, be 3-20 characters long, " +
                            "and can contain lower case letters, digits, dots, or underscores");
        }


        // checking email exists or not
        if(authRepository.existsByEmail(body.getEmail())){
            return new APIFailResponse(HttpStatus.NOT_ACCEPTABLE, "Not a valid Email");
        }


        // hashing password
        String hashedPassword = BcryptUtils.hash(body.getPassword());
        body.setPassword(hashedPassword);
        User user = new User(body.getUsername(), body.getEmail(), body.getPassword());
        User savedUser = authRepository.save(user);


        // generating token for verification
        String token = TokenUtils.generateToken(16);
        String oneTimePassword = TokenUtils.getOneTimePassword();

        VerifyAuth verifyAuth;


        if(verifyAuthRepository.existsByUser_Email(body.getEmail())) {
            verifyAuth = verifyAuthRepository.findByEmail(body.getEmail());
        }else{
            verifyAuth = new VerifyAuth();
        }

        // Saving token inside the auth
        verifyAuth.setToken(token);
        verifyAuth.setPassword(BcryptUtils.hash(oneTimePassword));
        verifyAuth.setUser(savedUser);
        verifyAuth.setExpiryTime(LocalDateTime.now().plusMinutes(20));
        verifyAuthRepository.save(verifyAuth);


        // Sending the password to the user
        mailService.sendMail(savedUser.getEmail(), "Verify your email", MailBodyBuilder.buildVerificationEmail(savedUser.getUsername(), oneTimePassword));

        return new APISuccessResponse<>(true, HttpStatus.CREATED, "User created successfully, Verify to continue",new TokenSchema(token));
//        return null;
    }
    public APIResponseBody verifyUser(String token, String OTP, HttpServletResponse response){
        if(!verifyAuthRepository.existsByToken(token)){
            return new APIFailResponse(HttpStatus.NOT_FOUND, "Invalid Token");
        }

        VerifyAuth verifyAuth = verifyAuthRepository.findByToken(token);

        if(LocalDateTime.now().isAfter(verifyAuth.getExpiryTime())){
            return new APIFailResponse(HttpStatus.NOT_ACCEPTABLE, "Time limit exceeded");
        }

        if(!BcryptUtils.checkHash(OTP, verifyAuth.getPassword())){
            return new APIFailResponse(HttpStatus.NOT_ACCEPTABLE, "Invalid Token");
        }

        User user = verifyAuth.getUser();
        user.setVerified(true);

        authRepository.save(user);

        verifyAuthRepository.delete(verifyAuth);


        String t = jwtUtils.generateToken(user.getId(), user.getUserRole().toString(), user.getEmail(), user.getUsername());


        Cookie cookie = new Cookie("jwt",t);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*7);
        response.addCookie(cookie);


        return new APISuccessResponse<UserResponseSchema>(true, HttpStatus.ACCEPTED,
                "User verified successfully",
                new UserResponseSchema(user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.isVerified(),
                        user.getUserRole()));
    }


    public APIResponseBody getMe(String token){
        if (token == null || token.isEmpty()) {
            return new APIFailResponse(HttpStatus.UNAUTHORIZED, "Unauthorized: No token found");
        }
        String userId;
        try {
            userId = jwtUtils.extractUserId(token).getSubject();
        } catch (Exception e) {

            return new APIFailResponse(HttpStatus.UNAUTHORIZED, "Invalid or expired token");

        }

        User user = authRepository.findById(userId).orElse(null);
        if (user == null) {
            return new APIFailResponse(HttpStatus.NOT_FOUND, "User not found");
        }

        return new APISuccessResponse<UserResponseSchema>(
                true,
                HttpStatus.OK,
                "User fetched successfully",
                new UserResponseSchema(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.isVerified(),
                        user.getUserRole()
                )
        );
    }
    public APIResponseBody forgotPasswordOrChangePassword(String email){
        if(!authRepository.existsByEmail(email)){
            return new APIFailResponse(HttpStatus.NOT_FOUND, "User not found with this email");
        }

        User user = authRepository.findByEmail(email);
        String token = TokenUtils.generateToken(16);

        resetPasswordRepository.save(new ResetPassword(token, user, LocalDateTime.now().plusMinutes(20)));

        mailService.sendMail(email,"Reset Password", MailBodyBuilder.buildResetPasswordEmail(user.getUsername(), token));

        return new APISuccessResponse<>(HttpStatus.ACCEPTED,"Reset password link has been sent your email", null);
    }
    public APIResponseBody resetPassword(String token, String password){
        if(! resetPasswordRepository.existsByResetPasswordToken(token)){
            return new APIFailResponse(HttpStatus.NOT_FOUND, "Invalid Token");
        }
        if(password.isEmpty() ||
                !password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$")) {
            return new APIFailResponse(HttpStatus.NOT_ACCEPTABLE,
                    "Password must be at least 8 characters long, " +
                            "contain uppercase, lowercase, digit, and special character");
        }
        ResetPassword resetPassword = resetPasswordRepository.findByResetPasswordToken(token);
        User user = resetPassword.getUser();
        user.setPassword(BcryptUtils.hash(password));
        authRepository.save(user);
        resetPasswordRepository.delete(resetPassword);
        return new APISuccessResponse<>(HttpStatus.ACCEPTED,"Password has been modified successfully", null);
    }
    public APIResponseBody resendOTP(String token){
        if(!verifyAuthRepository.existsByToken(token)){
            return new APIFailResponse(HttpStatus.NOT_FOUND, "Invalid Token");
        }

        VerifyAuth verifyAuth = verifyAuthRepository.findByToken(token);
        User user = verifyAuth.getUser();
        String OTP = TokenUtils.getOneTimePassword();
        verifyAuth.setPassword(BcryptUtils.hash(OTP));
        verifyAuthRepository.save(verifyAuth);
        mailService.sendMail(user.getEmail(), "Verification email", MailBodyBuilder.buildVerificationEmail(user.getUsername(), OTP));



        return new APISuccessResponse<>(HttpStatus.OK, "Otp has been sent to email", null);
    }



}
