package com.Group10.SocialMediaPlatform.Service;

import com.Group10.SocialMediaPlatform.model.Profile;
import com.Group10.SocialMediaPlatform.model.User;
import com.Group10.SocialMediaPlatform.Repository.ProfileRepository;
import com.Group10.SocialMediaPlatform.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    public Profile createProfile(Profile profile, Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (profileRepository.existsByUser(user)) {
                throw new IllegalArgumentException("User already has a profile.");
            }
            profile.setUser(user);
            return profileRepository.save(profile);
        } else {
            throw new IllegalArgumentException("User not found.");
        }
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Profile getProfileByUserId(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return profileRepository.findByUser(user).orElse(null);
        } else {
            throw new IllegalArgumentException("User not found.");
        }
    }

    public Profile updateProfileByUserId(Profile profile, Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Profile> existingProfileOptional = profileRepository.findByUser(user);
            if (existingProfileOptional.isPresent()) {
                Profile existingProfile = existingProfileOptional.get();
                existingProfile.setBio(profile.getBio());
                existingProfile.setProfilePicture(profile.getProfilePicture());
                return profileRepository.save(existingProfile);
            } else {
                throw new IllegalArgumentException("Profile not found.");
            }
        } else {
            throw new IllegalArgumentException("User not found.");
        }
    }
}
