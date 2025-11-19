package com.nts.profile.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileSchema {
    private String firstName;
    private String lastName;
    private String bio;
    private String address;
}
