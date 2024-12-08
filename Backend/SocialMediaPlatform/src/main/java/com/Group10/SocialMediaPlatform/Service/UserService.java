package com.Group10.SocialMediaPlatform.Service;

import com.Group10.SocialMediaPlatform.Repository.FriendRepository;
import com.Group10.SocialMediaPlatform.Repository.UserRepository;
import com.Group10.SocialMediaPlatform.model.Friend;
import com.Group10.SocialMediaPlatform.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //@Autowired
    //private PasswordEncoder passwordEncoder;

    @Autowired
    private FriendRepository friendRepository;

    public User createUser(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        user.setApproved(true);
        return userRepository.save(user);
    }

    public User createApprovedUser(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // Set the user as approved by default
        user.setApproved(true);

        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = findUserByEmail(email);
        validatePassword(user, password);
        return user;
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    }

    private void validatePassword(User user, String password) {
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }


    public User resetPassword(String email, String securityAnswer, String newPassword) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getSecurityAnswer().equals(securityAnswer)) {
            user.get().setPassword(newPassword);
            return userRepository.save(user.get());
        } else {
            throw new IllegalArgumentException("Security answer is incorrect.");
        }
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllUsersExceptUserAndFriends(Integer userId) {
        List<User> users = getAllUsers();
        User currUser = getUserById(userId);

        users.remove(currUser);

        List<Friend> friendsForCurrentUser = friendRepository.findByUser_UserId(userId);

        for (Friend friend : friendsForCurrentUser) {
            users.remove(friend.getFriend());
        }

        return users;
    }

    public User updateUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            user.setPassword(user.getPassword());
            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found.");
        }
    }

   public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }
    public List<User> getPendingApprovals() {
        return userRepository.findByApprovedFalse();
    }

    public List<User> getApprovedUsers() {
        return userRepository.findByApprovedTrue();
    }

    public void approveUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setApproved(true);
        userRepository.save(user);
    }

    public void rejectUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    public void updateUserRole(Integer userId, String role) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRole(role);
        userRepository.save(user);
    }

    public void softDeleteUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setDeleted(true);
        userRepository.save(user);
    }
}
