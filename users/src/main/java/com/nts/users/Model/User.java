package com.nts.users.Model;


import com.nts.users.Model.Enum.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;


@Entity
@Table(name = "Auth_User")
@Data
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String username;
    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private boolean isVerified;

    public User(){}



    public User(String id, String username, String email, String password, boolean isVerified) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isVerified = isVerified;
        this.userRole = UserRole.USER;
    }
    public User( String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRole = UserRole.USER;
    }


}
