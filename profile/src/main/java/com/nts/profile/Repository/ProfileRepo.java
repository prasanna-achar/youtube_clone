package com.nts.profile.Repository;

import com.nts.profile.Models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProfileRepo extends JpaRepository<Profile, String> {
}
