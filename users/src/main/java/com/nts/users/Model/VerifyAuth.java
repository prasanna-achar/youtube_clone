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
public class VerifyAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String token;
    private String password;
    private LocalDateTime expiryTime;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;



}
