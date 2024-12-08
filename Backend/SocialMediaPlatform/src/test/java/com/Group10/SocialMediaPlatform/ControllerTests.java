package com.Group10.SocialMediaPlatform;

import com.Group10.SocialMediaPlatform.Controller.FriendController;
import com.Group10.SocialMediaPlatform.Controller.PostController;
import com.Group10.SocialMediaPlatform.Controller.ProfileController;
import com.Group10.SocialMediaPlatform.Controller.UserController;
import com.Group10.SocialMediaPlatform.Service.FriendService;
import com.Group10.SocialMediaPlatform.Service.PostService;
import com.Group10.SocialMediaPlatform.Service.ProfileService;
import com.Group10.SocialMediaPlatform.Service.UserService;
import com.Group10.SocialMediaPlatform.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;
    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setSecurityAnswer("securityAnswer");
    }

    @Test
    void testCreateUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/signup")
                        .contentType("application/json")
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\",\"securityAnswer\":\"securityAnswer\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void testCreateUserException() throws Exception {
        when(userService.createUser(any(User.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        mockMvc.perform(post("/api/users/signup")
                        .contentType("application/json")
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\",\"securityAnswer\":\"securityAnswer\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid data"));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void testLogin() throws Exception {
        // Prepare the mock user object
        User user = new User();
        user.setUserId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole("USER");
        user.setApproved(true);

        when(userService.login(anyString(), anyString())).thenReturn(user);

        mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(user.getUserId()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.role").value(user.getRole()));

        verify(userService, times(1)).login(anyString(), anyString());
    }

    @Test
    void testLoginUserNotApproved() throws Exception {
        // Prepare the mock user object
        User user = new User();
        user.setUserId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole("USER");
        user.setApproved(false);

        when(userService.login(anyString(), anyString())).thenReturn(user);

        mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("User not approved"));

        verify(userService, times(1)).login(anyString(), anyString());
    }

    @Test
    void testLoginWrongCredentials() throws Exception {
        when(userService.login(anyString(), anyString())).thenThrow(new RuntimeException("Wrong email or password"));

        mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content("{\"email\":\"test@example.com\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Wrong email or password"));

        verify(userService, times(1)).login(anyString(), anyString());
    }


    @Test
    void testLoginException() throws Exception {
        when(userService.login(anyString(), anyString())).thenThrow(new IllegalArgumentException("Invalid email or password"));

        mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content("{\"email\":\"test@example.com\",\"password\":\"wrongPassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Wrong email or password"));

        verify(userService, times(1)).login(anyString(), anyString());
    }

    @Test
    void testResetPassword() throws Exception {
        when(userService.resetPassword(anyString(), anyString(), anyString())).thenReturn(user);

        mockMvc.perform(post("/api/users/reset-password")
                        .contentType("application/json")
                        .content("{\"email\":\"test@example.com\",\"securityAnswer\":\"securityAnswer\",\"password\":\"newPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).resetPassword(anyString(), anyString(), anyString());
    }

    @Test
    void testResetPasswordException() throws Exception {
        when(userService.resetPassword(anyString(), anyString(), anyString())).thenThrow(new IllegalArgumentException("Security answer is incorrect."));

        mockMvc.perform(post("/api/users/reset-password")
                        .contentType("application/json")
                        .content("{\"email\":\"test@example.com\",\"securityAnswer\":\"wrongAnswer\",\"password\":\"newPassword\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Security answer is incorrect."));

        verify(userService, times(1)).resetPassword(anyString(), anyString(), anyString());
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getUserById(anyInt())).thenReturn(user);

        mockMvc.perform(get("/api/users/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).getUserById(anyInt());
    }

    @Test
    void testGetUsersExceptUserId() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userService.getAllUsersExceptUserAndFriends(anyInt())).thenReturn(users);

        mockMvc.perform(get("/api/users/getOther/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@example.com"));

        verify(userService, times(1)).getAllUsersExceptUserAndFriends(anyInt());
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@example.com"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userService.updateUser(any(User.class))).thenReturn(user);

        mockMvc.perform(patch("/api/users/1")
                        .contentType("application/json")
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\",\"securityAnswer\":\"securityAnswer\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).updateUser(any(User.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(anyInt());

        mockMvc.perform(delete("/api/users/1")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(anyInt());
    }
}
@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private ProfileService profileService;

    private MockMvc mockMvc;
    private Profile profile;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(profileController).build();
        profile = new Profile();
        profile.setProfileId(1);
        profile.setUser(new User()); // Initialize with a user object
        profile.setBio("This is a bio");
        profile.setProfilePicture("picture_url");
        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateProfile() throws Exception {
        when(profileService.createProfile(any(Profile.class), anyInt())).thenReturn(profile);

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bio\":\"This is a bio\",\"profilePicture\":\"picture_url\"}")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bio").value("This is a bio"))
                .andExpect(jsonPath("$.profilePicture").value("picture_url"));

        verify(profileService, times(1)).createProfile(any(Profile.class), anyInt());
    }

    @Test
    void testGetAllProfiles() throws Exception {
        List<Profile> profiles = new ArrayList<>();
        profiles.add(profile);
        when(profileService.getAllProfiles()).thenReturn(profiles);

        mockMvc.perform(get("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bio").value("This is a bio"))
                .andExpect(jsonPath("$[0].profilePicture").value("picture_url"));

        verify(profileService, times(1)).getAllProfiles();
    }

    @Test
    void testGetProfileByUserId() throws Exception {
        when(profileService.getProfileByUserId(anyInt())).thenReturn(profile);

        mockMvc.perform(get("/api/profiles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bio").value("This is a bio"))
                .andExpect(jsonPath("$.profilePicture").value("picture_url"));

        verify(profileService, times(1)).getProfileByUserId(anyInt());
    }

    @Test
    void testUpdateProfile() throws Exception {
        when(profileService.updateProfileByUserId(any(Profile.class), anyInt())).thenReturn(profile);

        mockMvc.perform(patch("/api/profiles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bio\":\"Updated bio\",\"profilePicture\":\"updated_picture_url\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bio").value("This is a bio"))
                .andExpect(jsonPath("$.profilePicture").value("picture_url"));

        verify(profileService, times(1)).updateProfileByUserId(any(Profile.class), anyInt());
    }
}
@ExtendWith(MockitoExtension.class)
class FriendControllerTest {

    @InjectMocks
    private FriendController friendController;

    @Mock
    private FriendService friendService;

    private Friend friend;
    private List<Friend> friends;

    @BeforeEach
    void setUp() {
        friend = new Friend();
        friend.setId(new FriendId(1, 2));
        friend.setStatus("accepted");

        friends = new ArrayList<>();
        friends.add(friend);
    }

    @Test
    void testAddFriend() {
        when(friendService.addFriend(any(Friend.class))).thenReturn(friend);

        Friend result = friendController.addFriend(friend);

        assertNotNull(result);
        assertEquals(friend, result);
        verify(friendService, times(1)).addFriend(any(Friend.class));
    }

    @Test
    void testGetFriendsByUserId() {
        when(friendService.getFriendsByUserId(anyInt())).thenReturn(friends);

        List<Friend> result = friendController.getFriendsByUserId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(friend, result.get(0));
        verify(friendService, times(1)).getFriendsByUserId(anyInt());
    }

    @Test
    void testGetAllFriendsForUser() {
        when(friendService.getAllFriendsForUser(anyInt())).thenReturn(friends);

        List<Friend> result = friendController.getAllFriendsForUser(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(friend, result.get(0));
        verify(friendService, times(1)).getAllFriendsForUser(anyInt());
    }

    @Test
    void testRemoveFriend() {
        doNothing().when(friendService).removeFriend(anyInt(), anyInt());

        assertDoesNotThrow(() -> friendController.removeFriend(1, 2));
        verify(friendService, times(1)).removeFriend(anyInt(), anyInt());
    }
}
class PostControllerTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    private Post post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        post = new Post();
        post.setPostId(1);
        post.setContent("This is a test post.");
    }

    @Test
    void testCreatePost() {
        when(postService.createPost(any(Post.class))).thenReturn(post);

        Post createdPost = postController.createPost(post);

        assertNotNull(createdPost);
        assertEquals(post.getPostId(), createdPost.getPostId());
        verify(postService, times(1)).createPost(post);
    }

    @Test
    void testGetPostById() {
        when(postService.getPostById(1)).thenReturn(post);

        Post retrievedPost = postController.getPostById(1);

        assertNotNull(retrievedPost);
        assertEquals(post.getPostId(), retrievedPost.getPostId());
        verify(postService, times(1)).getPostById(1);
    }

    @Test
    void testGetAllPosts() {
        List<Post> posts = Arrays.asList(post);
        when(postService.getAllPosts()).thenReturn(posts);

        List<Post> retrievedPosts = postController.getAllPosts();

        assertNotNull(retrievedPosts);
        assertEquals(1, retrievedPosts.size());
        verify(postService, times(1)).getAllPosts();
    }

    @Test
    void testDeletePost() {
        doNothing().when(postService).deletePost(1);

        postController.deletePost(1);

        verify(postService, times(1)).deletePost(1);
    }
//
//    @Test
//    void testCreatePostThrowsException() {
//        when(postService.createPost(any(Post.class))).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
//
//        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
//            postController.createPost(post);
//        });
//
//        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
//    }
//
//    @Test
//    void testGetPostByIdNotFound() {
//        when(postService.getPostById(999)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
//            postController.getPostById(999);
//        });
//
//        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
//    }
}