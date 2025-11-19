package com.nts.profile.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    private String userId;

    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private String address;
    private String userRole;
    private String avatarUrl;



}
