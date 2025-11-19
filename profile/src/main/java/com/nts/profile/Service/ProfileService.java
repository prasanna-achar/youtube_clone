package com.nts.profile.Service;


import com.nts.profile.DTO.Internal.ProfileIntSchema;
import com.nts.profile.DTO.Response.UserProfile;
import com.nts.profile.Models.Profile;
import com.nts.profile.Repository.ProfileRepo;
import com.nts.profile.ResponseBody.APIFailResponse;
import com.nts.profile.ResponseBody.APIResponseBody;
import com.nts.profile.ResponseBody.APISuccessResponse;
import com.nts.profile.Utils.CloudinaryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProfileService {

    @Autowired
    ProfileRepo profileRepo;

    private String UPLOAD_DIR = "/uploads";

    @Autowired
    CloudinaryUtils cloudinaryUtils;


    public APIResponseBody saveProfile(ProfileIntSchema internalProfileSchema){
        Profile profile;
        if(!profileRepo.existsById(internalProfileSchema.getUserId())){
            profile = new Profile(internalProfileSchema.getUserId(),
                    internalProfileSchema.getEmail(),
                    internalProfileSchema.getUsername(),
                    internalProfileSchema.getFirstName(),
                    internalProfileSchema.getLastName(),
                    internalProfileSchema.getBio(),
                    internalProfileSchema.getAddress(),
                    internalProfileSchema.getUserRole(),
                    null);
        }else{
            profile = profileRepo.findById(internalProfileSchema.getUserId()).get();
            profile.setFirstName(internalProfileSchema.getFirstName());
            profile.setLastName(internalProfileSchema.getLastName());
            profile.setEmail(internalProfileSchema.getEmail());
            profile.setUsername(internalProfileSchema.getUsername());
            profile.setBio(internalProfileSchema.getBio());
            profile.setUserRole(profile.getUserRole());
        }

        Profile savedProfile = profileRepo.save(profile);
        return new APISuccessResponse<Profile>("Profile has been saved",savedProfile);
    }


    public APIResponseBody uploadAvatar(String userId, MultipartFile file)  {
        Profile profile;
        if(!profileRepo.existsById(userId)){
            profile = new Profile();
            profile.setUserId(userId);
        }else{
            profile = profileRepo.findById(userId).get();
        }
        String avatarUrl = null ;
        try {

            File imageDir = new File(UPLOAD_DIR+"/avatar");
            if(!imageDir.exists()) imageDir.mkdirs();

            String originalName = file.getOriginalFilename();
            String extension="";
            if(originalName != null && originalName.contains(".")){
                extension = originalName.substring(originalName.lastIndexOf("."));
            }

            String filename = userId +"_" +System.currentTimeMillis()  + extension;
            Path path = Paths.get(imageDir.getAbsolutePath() ,filename );
            Files.write(path, file.getBytes());
            //        try{
//         avatarUrl = cloudinaryUtils.uploadImage(file, "/avatar", userId);
//
//        }catch(IOException ex){
//            System.out.println(ex.getMessage());
//            throw new RuntimeException(ex);
            avatarUrl = cloudinaryUtils.uploadImage(path, "avatars", userId);
//        }

        }catch (IOException ex){
            System.out.println("IO Exception occured");
            System.out.println(ex.getLocalizedMessage());
        }

        catch (Exception ex){
            System.out.println(" Exception occured");
            System.out.println(ex.getLocalizedMessage());
        }


        profile.setAvatarUrl(avatarUrl);
        profileRepo.save(profile);
        return new APISuccessResponse<>("Profile Pic uploaded successfully", profile);
    }

    public APIResponseBody getMyProfile(String userId){
        if(!profileRepo.existsById(userId)){
            return new APISuccessResponse<>(
                    "Profile details not added",
                    new UserProfile()
            );
        }
        return new APISuccessResponse<>(
                "User Profile fetched successfully",
                profileRepo.findById(userId).get()
        );
    }

    public APIResponseBody getUserProfile(String userId){
        if(!profileRepo.existsById(userId)){
            return new APISuccessResponse<>(
                    "Profile details not added",
                    new UserProfile()
            );
        }


        return new APISuccessResponse<>(
                "User profile has been successfully fetched",
                new UserProfile(
                        profileRepo.findById(userId).get()
                )
        );
    }
}
