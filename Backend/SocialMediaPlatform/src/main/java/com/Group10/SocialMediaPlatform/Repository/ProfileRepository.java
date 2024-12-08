package com.Group10.SocialMediaPlatform.Repository;

import com.Group10.SocialMediaPlatform.model.Profile;
import com.Group10.SocialMediaPlatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Optional<Profile> findByUser(User user);
    boolean existsByUser(User user);

}
