package com.nts.users.Repository;

import com.nts.users.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository  extends JpaRepository<User, String> {
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
    public User findByUsername(String username);
    public User findByEmail(String email);
}
