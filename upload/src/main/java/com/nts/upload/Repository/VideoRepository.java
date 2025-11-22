package com.nts.upload.Repository;

import com.nts.upload.Model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, String> {

    boolean existsByToken(String s);
    Optional<Video> findByToken(String token);
    List<Video> findAllByUserId(String userId);
}
