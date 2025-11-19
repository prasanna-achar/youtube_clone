package com.nts.users.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String resetPasswordToken;

    private LocalDateTime expiryTime;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;


    public ResetPassword(String id, String resetPasswordToken, User user, LocalDateTime expiryTime) {
        this.id = id;
        this.resetPasswordToken = resetPasswordToken;
        this.user = user;
        this.expiryTime = expiryTime;
    }
    public ResetPassword( String resetPasswordToken, User user, LocalDateTime expiryTime) {
        this.resetPasswordToken = resetPasswordToken;
        this.user = user;
        this.expiryTime = expiryTime;
    }


}
