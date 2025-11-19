package com.nts.profile.DTO.Internal;


import com.nts.profile.DTO.ProfileSchema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileIntSchema {
    private String userId;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private String address;
    private String userRole;

    public ProfileIntSchema(String userId, String email, String username, String userRole, ProfileSchema profileSchema){
        this(userId,
                email,
                username,
                profileSchema.getFirstName(),
                profileSchema.getLastName(),
                profileSchema.getBio(),
                profileSchema.getAddress(),
                userRole);
    }
}
