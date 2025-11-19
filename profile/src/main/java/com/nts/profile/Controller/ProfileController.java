package com.nts.profile.Controller;

import com.nts.profile.DTO.Internal.ProfileIntSchema;
import com.nts.profile.DTO.ProfileSchema;
import com.nts.profile.Models.Profile;
import com.nts.profile.ResponseBody.APIResponseBody;
import com.nts.profile.Service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @PostMapping("/save")
    public ResponseEntity<APIResponseBody> createProfile(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Role") String userRole,
            @RequestHeader("X-User-Name") String username,
            @RequestBody ProfileSchema profile){

        System.out.println(profile.getFirstName());

        ProfileIntSchema profileDetails = new ProfileIntSchema(userId, email, username, userRole, profile);

        System.out.println(profileDetails.getFirstName());

        APIResponseBody responseBody = profileService.saveProfile(profileDetails);
        return new ResponseEntity<>(responseBody, responseBody.getHttpStatus());
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<APIResponseBody> uploadAvatar(@RequestHeader("X-User-Id") String userId, MultipartFile file){

        APIResponseBody responseBody = profileService.uploadAvatar(userId, file);
        return new ResponseEntity<>(responseBody, responseBody.getHttpStatus());
    }

    @GetMapping
    public ResponseEntity<APIResponseBody> getMyProfile(@RequestHeader("X-User-Id") String userId){
        APIResponseBody responseBody = profileService.getMyProfile(userId);
        return new ResponseEntity<>(responseBody, responseBody.getHttpStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponseBody> getUserProfile(@PathVariable String id){
        APIResponseBody responseBody = profileService.getUserProfile(id);
        return new ResponseEntity<>(responseBody, responseBody.getHttpStatus());
    }
}
