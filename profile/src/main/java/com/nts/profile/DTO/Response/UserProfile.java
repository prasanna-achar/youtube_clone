package com.nts.profile.DTO.Response;

import com.nts.profile.Models.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    private String userId;
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private String address;
    private String userRole;
    private String avatarUrl;

    public UserProfile(Profile profile){
        this(
                profile.getUserId(),
                profile.getUsername(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getBio(),
                profile.getAddress(),
                profile.getUserRole(),
                profile.getAvatarUrl()
        );
    }

}
