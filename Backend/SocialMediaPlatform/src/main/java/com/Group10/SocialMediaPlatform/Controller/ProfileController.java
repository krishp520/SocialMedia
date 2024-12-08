package com.Group10.SocialMediaPlatform.Controller;

import com.Group10.SocialMediaPlatform.model.Profile;
import com.Group10.SocialMediaPlatform.Service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping
    public Profile createProfile(@RequestBody Profile profile, @RequestParam Integer userId) {
        return profileService.createProfile(profile, userId);
    }

    @GetMapping
    public List<Profile> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("/{userId}")
    public Profile getProfileByUserId(@PathVariable Integer userId) {
        return profileService.getProfileByUserId(userId);
    }

    @PatchMapping("/{userId}")
    public Profile updateProfile(@PathVariable Integer userId, @RequestBody Profile profile) {
        return profileService.updateProfileByUserId(profile, userId);
    }
}
