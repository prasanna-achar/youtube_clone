package com.nts.users.Repository;

import com.nts.users.Model.VerifyAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface VerifyAuthRepository extends JpaRepository<VerifyAuth, String> {
    public boolean existsByUser_Email(String email);
    @Query("SELECT v FROM VerifyAuth v WHERE v.user.email = :email")
    public VerifyAuth findByEmail(String email);
    public boolean existsByToken(String token);
    public  VerifyAuth findByToken(String token);
}
