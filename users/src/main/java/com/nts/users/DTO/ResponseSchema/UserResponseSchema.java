package com.nts.users.DTO.ResponseSchema;


import com.nts.users.Model.Enum.UserRole;
import com.nts.users.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseSchema {
    private String id;
    private String username;
    private String email;
    private boolean verified;
    private UserRole userRole;


    public UserResponseSchema(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.verified = user.isVerified();
        this.userRole = user.getUserRole();
    }
}
