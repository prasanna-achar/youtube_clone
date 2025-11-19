package com.nts.users.Repository;

import com.nts.users.Model.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, String> {
    public boolean existsByUser_Email(String email);
    @Query("SELECT v FROM VerifyAuth v WHERE v.user.email = :email")
    public ResetPassword findByEmail(String email);
    public boolean existsByResetPasswordToken(String token);
    public ResetPassword findByResetPasswordToken(String token);
}
