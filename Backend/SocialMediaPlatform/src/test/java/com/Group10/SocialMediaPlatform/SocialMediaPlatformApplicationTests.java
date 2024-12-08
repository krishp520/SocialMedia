package com.Group10.SocialMediaPlatform;

import com.Group10.SocialMediaPlatform.Repository.*;
import com.Group10.SocialMediaPlatform.Service.*;
import com.Group10.SocialMediaPlatform.model.*;
import com.Group10.SocialMediaPlatform.Repository.FriendRepository;
import com.Group10.SocialMediaPlatform.model.Friend;
import com.Group10.SocialMediaPlatform.model.FriendId;
import com.Group10.SocialMediaPlatform.model.User;
import com.Group10.SocialMediaPlatform.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class CommentServiceTest {

	@Mock
	private CommentRepository commentRepository;

	@InjectMocks
	private CommentService commentService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testCreateComment() {
		Comment comment = new Comment();

		when(commentRepository.save(any(Comment.class))).thenReturn(comment);

		Comment createdComment = commentService.createComment(comment);

		assertNotNull(createdComment);
		/*assertEquals("Test comment", createdComment.getContent());*/
		verify(commentRepository, times(1)).save(comment);
	}

	@Test
	void testGetCommentById() {
		Integer commentId = 1;
		Comment comment = new Comment();
		comment.setCommentId(commentId);

		when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

		Comment foundComment = commentService.getCommentById(commentId);

		assertNotNull(foundComment);
		assertEquals(commentId, foundComment.getCommentId());
		/*assertEquals("Test comment", foundComment.getContent());*/
		verify(commentRepository, times(1)).findById(commentId);
	}

	@Test
	void testGetAllComments() {
		Comment comment1 = new Comment();
		comment1.setContent("Comment 1");
		Comment comment2 = new Comment();
		comment2.setContent("Comment 2");

		when(commentRepository.findAll()).thenReturn(Arrays.asList(comment1, comment2));

		List<Comment> comments = commentService.getAllComments();

		assertNotNull(comments);
		assertEquals(2, comments.size());
		assertEquals("Comment 1", comments.get(0).getContent());
		assertEquals("Comment 2", comments.get(1).getContent());
		verify(commentRepository, times(1)).findAll();
	}
}

class FriendServiceTest {

	@Mock
	private FriendRepository friendRepository;

	@InjectMocks
	private FriendService friendService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testAddFriend() {
		FriendId friendId = new FriendId(1, 2);
		User user = new User();
		user.setUserId(1);
		User friendUser = new User();
		friendUser.setUserId(2);
		Friend friend = new Friend(friendId, "accepted", null, user, friendUser);

		when(friendRepository.save(any(Friend.class))).thenReturn(friend);

		Friend addedFriend = friendService.addFriend(friend);

		assertNotNull(addedFriend);
		assertEquals(1, addedFriend.getId().getUserId());
		assertEquals(2, addedFriend.getId().getFriendId());
		verify(friendRepository, times(1)).save(friend);
	}

	@Test
	void testGetFriendsByUserId() {
		Integer userId = 1;
		User user = new User();
		user.setUserId(userId);
		User friendUser1 = new User();
		friendUser1.setUserId(2);
		User friendUser2 = new User();
		friendUser2.setUserId(3);

		Friend friend1 = new Friend(new FriendId(userId, 2), "accpeted", null, user, friendUser1);
		Friend friend2 = new Friend(new FriendId(userId, 3), "accepted", null, user, friendUser2);

		when(friendRepository.findByUser_UserId(userId)).thenReturn(Arrays.asList(friend1, friend2));

		List<Friend> friends = friendService.getFriendsByUserId(userId);

		assertNotNull(friends);
		assertEquals(2, friends.size());
		assertEquals(2, friends.get(0).getId().getFriendId());
		assertEquals(3, friends.get(1).getId().getFriendId());
		verify(friendRepository, times(1)).findByUser_UserId(userId);
	}

	@Test
	void testRemoveFriend() {
		FriendId friendId = new FriendId(1, 2);

		doNothing().when(friendRepository).deleteById(friendId);

		friendService.removeFriend(1,2);

		verify(friendRepository, times(1)).deleteById(friendId);
	}

	@Test
	void testGetAllFriendsForUser() {
		Integer userId = 1;
		User user = new User();
		user.setUserId(userId);
		User friendUser1 = new User();
		friendUser1.setUserId(2);
		User friendUser2 = new User();
		friendUser2.setUserId(3);

		Friend friend1 = new Friend(new FriendId(userId, 2), "accepted", null, user, friendUser1);
		Friend friend2 = new Friend(new FriendId(2, userId), "accepted", null, friendUser1, user);
		Friend friend3 = new Friend(new FriendId(userId, 3), "accepted", null, user, friendUser2);
		Friend friend4 = new Friend(new FriendId(3, userId), "accepted", null, friendUser2, user);

		when(friendRepository.findByUser_UserId(userId)).thenReturn(Arrays.asList(friend1, friend3));
		when(friendRepository.findByFriend_UserId(userId)).thenReturn(Arrays.asList(friend2, friend4));

		List<Friend> friends = friendService.getAllFriendsForUser(userId);

		assertNotNull(friends);
		assertEquals(4, friends.size());
		verify(friendRepository, times(1)).findByUser_UserId(userId);
		verify(friendRepository, times(1)).findByFriend_UserId(userId);
	}
}

class PostServiceTest {

	@Mock
	private PostRepository postRepository;

	@InjectMocks
	private PostService postService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testCreatePost() {
		Post post = new Post();
		post.setContent("Test post content");

		when(postRepository.save(any(Post.class))).thenReturn(post);

		Post createdPost = postService.createPost(post);

		assertNotNull(createdPost);
		assertEquals("Test post content", createdPost.getContent());
		verify(postRepository, times(1)).save(post);
	}

	@Test
	void testGetPostById() {
		Integer postId = 1;
		Post post = new Post();
		post.setPostId(postId);
		post.setContent("Test post content");

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));

		Post foundPost = postService.getPostById(postId);

		assertNotNull(foundPost);
		assertEquals(postId, foundPost.getPostId());
		assertEquals("Test post content", foundPost.getContent());
		verify(postRepository, times(1)).findById(postId);
	}

	@Test
	void testGetAllPosts() {
		Post post1 = new Post();
		post1.setContent("Post 1");
		Post post2 = new Post();
		post2.setContent("Post 2");

		when(postRepository.findAll()).thenReturn(Arrays.asList(post1, post2));

		List<Post> posts = postService.getAllPosts();

		assertNotNull(posts);
		assertEquals(2, posts.size());
		assertEquals("Post 1", posts.get(0).getContent());
		assertEquals("Post 2", posts.get(1).getContent());
		verify(postRepository, times(1)).findAll();
	}
}

class ProfileServiceTest {

	@Mock
	private ProfileRepository profileRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private ProfileService profileService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testCreateProfile() {
		Integer userId = 1;
		User user = new User();
		user.setUserId(userId);

		Profile profile = new Profile();
		profile.setBio("Test bio");

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(profileRepository.existsByUser(user)).thenReturn(false);
		when(profileRepository.save(any(Profile.class))).thenReturn(profile);

		Profile createdProfile = profileService.createProfile(profile, userId);

		assertNotNull(createdProfile);
		assertEquals("Test bio", createdProfile.getBio());
		verify(userRepository, times(1)).findById(userId);
		verify(profileRepository, times(1)).existsByUser(user);
		verify(profileRepository, times(1)).save(profile);
	}

	@Test
	void testGetProfileByUserId() {
		Integer userId = 1;
		User user = new User();
		user.setUserId(userId);

		Profile profile = new Profile();
		profile.setUser(user);
		profile.setBio("Test bio");

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));

		Profile foundProfile = profileService.getProfileByUserId(userId);

		assertNotNull(foundProfile);
		assertEquals("Test bio", foundProfile.getBio());
		verify(userRepository, times(1)).findById(userId);
		verify(profileRepository, times(1)).findByUser(user);
	}

	@Test
	void testUpdateProfileByUserId() {
		Integer userId = 1;
		User user = new User();
		user.setUserId(userId);

		Profile existingProfile = new Profile();
		existingProfile.setUser(user);
		existingProfile.setBio("Old bio");

		Profile updatedProfile = new Profile();
		updatedProfile.setBio("Updated bio");
		updatedProfile.setProfilePicture("updated_picture.png");

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(profileRepository.findByUser(user)).thenReturn(Optional.of(existingProfile));
		when(profileRepository.save(any(Profile.class))).thenReturn(existingProfile);

		Profile result = profileService.updateProfileByUserId(updatedProfile, userId);

		assertNotNull(result);
		assertEquals("Updated bio", result.getBio());
		assertEquals("updated_picture.png", result.getProfilePicture());
		verify(userRepository, times(1)).findById(userId);
		verify(profileRepository, times(1)).findByUser(user);
		verify(profileRepository, times(1)).save(existingProfile);
	}
}

class RequestServiceTest {

	@Mock
	private RequestRepository requestRepository;

	@Mock
	private FriendRepository friendRepository;

	@InjectMocks
	private RequestService requestService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testSendRequest() {
		Request request = new Request();
		request.setStatus("pending");

		when(requestRepository.save(any(Request.class))).thenReturn(request);

		Request sentRequest = requestService.sendRequest(request);

		assertNotNull(sentRequest);
		assertEquals("pending", sentRequest.getStatus());
		verify(requestRepository, times(1)).save(request);
	}

	@Test
	void testHandleRequest() {
		Integer userId = 1;
		Integer requestId = 1;

		User sender = new User();
		sender.setUserId(2);

		User receiver = new User();
		receiver.setUserId(userId);

		Request request = new Request();
		request.setRequestId(requestId);
		request.setSender(sender);
		request.setReceiver(receiver);
		request.setStatus("pending");

		when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));

		requestService.handleRequest(userId, requestId, "accepted");

		assertEquals("accepted", request.getStatus());
		verify(friendRepository, times(2)).save(any(Friend.class));
		verify(requestRepository, times(1)).delete(request);
	}

	@Test
	void testGetRequestsByUserId() {
		Integer userId = 1;
		Request request1 = new Request();
		request1.setRequestId(1);
		Request request2 = new Request();
		request2.setRequestId(2);

		when(requestRepository.findByReceiverUserId(userId)).thenReturn(Arrays.asList(request1, request2));

		List<Request> requests = requestService.getRequestsByUserId(userId);

		assertNotNull(requests);
		assertEquals(2, requests.size());
		assertEquals(1, requests.get(0).getRequestId());
		assertEquals(2, requests.get(1).getRequestId());
		verify(requestRepository, times(1)).findByReceiverUserId(userId);
	}
}

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private FriendRepository friendRepository;

	private User user;
	private Friend friend;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setUserId(1);
		user.setEmail("test@example.com");
		user.setPassword("password");
		user.setSecurityAnswer("securityAnswer");

		friend = new Friend();
		friend.setFriend(user);
	}

	@Test
	void testCreateUser() {
		when(userRepository.save(any(User.class))).thenReturn(user);

		User createdUser = userService.createUser(user);

		assertNotNull(createdUser);
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void testCreateUserWithoutPassword() {
		user.setPassword(null);
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			userService.createUser(user);
		});
		assertEquals("Password cannot be null or empty", exception.getMessage());
	}

	@Test
	void testLoginSuccessful() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

		User loggedInUser = userService.login("test@example.com", "password");

		assertNotNull(loggedInUser);
		verify(userRepository, times(1)).findByEmail("test@example.com");
	}

	@Test
	void testLoginInvalidPassword() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			userService.login("test@example.com", "wrongPassword");
		});
		assertEquals("Invalid email or password", exception.getMessage());
	}

	@Test
	void testLoginInvalidEmail() {
		when(userRepository.findByEmail("wrong@example.com")).thenReturn(Optional.empty());

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			userService.login("wrong@example.com", "password");
		});
		assertEquals("Invalid email or password", exception.getMessage());
	}

	@Test
	void testResetPassword() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
		when(userRepository.save(any(User.class))).thenReturn(user);

		User updatedUser = userService.resetPassword("test@example.com", "securityAnswer", "newPassword");

		assertNotNull(updatedUser);
		assertEquals("newPassword", updatedUser.getPassword());
		verify(userRepository, times(1)).findByEmail("test@example.com");
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void testResetPasswordInvalidSecurityAnswer() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			userService.resetPassword("test@example.com", "wrongAnswer", "newPassword");
		});
		assertEquals("Security answer is incorrect.", exception.getMessage());
	}

	@Test
	void testGetUserById() {
		when(userRepository.findById(1)).thenReturn(Optional.of(user));

		User foundUser = userService.getUserById(1);

		assertNotNull(foundUser);
		verify(userRepository, times(1)).findById(1);
	}

	@Test
	void testGetUserByIdNotFound() {
		when(userRepository.findById(1)).thenReturn(Optional.empty());

		User foundUser = userService.getUserById(1);

		assertNull(foundUser);
		verify(userRepository, times(1)).findById(1);
	}

	@Test
	void testGetAllUsers() {
		List<User> users = new ArrayList<>();
		users.add(user);
		when(userRepository.findAll()).thenReturn(users);

		List<User> allUsers = userService.getAllUsers();

		assertEquals(1, allUsers.size());
		verify(userRepository, times(1)).findAll();
	}

	@Test
	void testGetAllUsersExceptUserAndFriends() {
		List<User> users = new ArrayList<>();
		users.add(user);
		when(userRepository.findAll()).thenReturn(users);
		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		List<Friend> friends = new ArrayList<>();
		friends.add(friend);
		when(friendRepository.findByUser_UserId(1)).thenReturn(friends);

		List<User> resultUsers = userService.getAllUsersExceptUserAndFriends(1);

		assertTrue(resultUsers.isEmpty());
		verify(userRepository, times(1)).findAll();
		verify(userRepository, times(1)).findById(1);
		verify(friendRepository, times(1)).findByUser_UserId(1);
	}

	@Test
	void testUpdateUser() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
		when(userRepository.save(any(User.class))).thenReturn(user);

		User updatedUser = userService.updateUser(user);

		assertNotNull(updatedUser);
		verify(userRepository, times(1)).findByEmail("test@example.com");
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void testUpdateUserNotFound() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			userService.updateUser(user);
		});
		assertEquals("User not found.", exception.getMessage());
	}

	@Test
	void testDeleteUser() {
		userService.deleteUser(1);

		verify(userRepository, times(1)).deleteById(1);
	}
}
@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

	@InjectMocks
	private GroupService groupService;

	@Mock
	private GroupRepository groupRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private GroupMemberRepository groupMemberRepository;

	private Group group;
	private User user;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setUserId(1);

		group = new Group();
		group.setGroupId(1);
		group.setCreatedBy(user);
		group.setIsPrivate(false);
	}

	@Test
	void testCreateGroup() {
		when(groupRepository.save(any(Group.class))).thenReturn(group);

		Group createdGroup = groupService.createGroup(group);

		assertNotNull(createdGroup);
		verify(groupRepository, times(1)).save(any(Group.class));
		verify(groupMemberRepository, times(1)).save(any(GroupMember.class));
	}

	@Test
	void testCreateGroupWithoutCreator() {
		group.setCreatedBy(null);
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			groupService.createGroup(group);
		});
		assertEquals("Group must have a creator", exception.getMessage());
	}

	@Test
	void testGetGroupByGroupId() {
		when(groupRepository.findById(1)).thenReturn(Optional.of(group));

		Group foundGroup = groupService.getGroupByGroupId(1);

		assertNotNull(foundGroup);
		verify(groupRepository, times(1)).findById(1);
	}

	@Test
	void testGetGroupByGroupIdNotFound() {
		when(groupRepository.findById(1)).thenReturn(Optional.empty());

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			groupService.getGroupByGroupId(1);
		});
		assertEquals("not found such group", exception.getMessage());
	}

	@Test
	void testUpdateInterests() {
		when(groupRepository.findById(1)).thenReturn(Optional.of(group));
		when(groupRepository.save(any(Group.class))).thenReturn(group);

		Group updatedGroup = groupService.updateInterests(1, "New Interests");

		assertEquals("New Interests", updatedGroup.getInterests());
		verify(groupRepository, times(1)).findById(1);
		verify(groupRepository, times(1)).save(any(Group.class));
	}

	@Test
	void testGetAllGroups() {
		List<Group> groups = new ArrayList<>();
		groups.add(group);
		when(groupRepository.findAll()).thenReturn(groups);

		List<Group> allGroups = groupService.getAllGroups();

		assertEquals(1, allGroups.size());
		verify(groupRepository, times(1)).findAll();
	}

	@Test
	void testGetGroupMembersByGroupId() {
		List<GroupMember> members = new ArrayList<>();
		GroupMember member = new GroupMember();
		member.setGroup(group);
		member.setUser(user);
		members.add(member);
		when(groupMemberRepository.findByIdGroupId(1)).thenReturn(members);

		List<GroupMember> groupMembers = groupService.getGroupMembersByGroupId(1);

		assertEquals(1, groupMembers.size());
		verify(groupMemberRepository, times(1)).findByIdGroupId(1);
	}

	@Test
	void testIsGroupMemberForCurrGroup() {
		GroupMemberId id = new GroupMemberId(1, 1);
		when(groupMemberRepository.findById(id)).thenReturn(Optional.of(new GroupMember()));

		boolean isMember = groupService.isGroupMemberForCurrGroup(1, 1);

		assertTrue(isMember);
		verify(groupMemberRepository, times(1)).findById(id);
	}

	@Test
	void testJoinPublicGroup() {
		when(groupRepository.findById(1)).thenReturn(Optional.of(group));
		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		when(groupMemberRepository.findById(any(GroupMemberId.class))).thenReturn(Optional.empty());

		groupService.joinPublicGroup(1, 1);

		verify(groupRepository, times(1)).findById(1);
		verify(userRepository, times(1)).findById(1);
		verify(groupMemberRepository, times(1)).save(any(GroupMember.class));
	}

	@Test
	void testJoinPublicGroupAlreadyMember() {
		when(groupRepository.findById(1)).thenReturn(Optional.of(group));
		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		GroupMemberId id = new GroupMemberId(1, 1);
		when(groupMemberRepository.findById(id)).thenReturn(Optional.of(new GroupMember()));

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			groupService.joinPublicGroup(1, 1);
		});
		assertEquals("you have already in this group", exception.getMessage());
	}

//	@Test
//	void testJoinPrivateGroup() {
//		group.setIsPrivate(true);
//		lenient().when(groupRepository.findById(1)).thenReturn(Optional.of(group));
//		lenient().when(userRepository.findById(1)).thenReturn(Optional.of(user));
//		when(groupMemberRepository.findById(any(GroupMemberId.class))).thenReturn(Optional.empty());
//
//		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//			groupService.joinPublicGroup(1, 1);
//		});
//		assertEquals("the group is private", exception.getMessage());
//	}

	@Test
	void testRemoveMemberFromCurrGroup() {
		GroupMemberId id = new GroupMemberId(1, 1);

		groupService.removeMemberFromCurrGroup(1, 1);

		verify(groupMemberRepository, times(1)).deleteById(id);
	}

	@Test
	void testUpdateGroupType() {
		when(groupRepository.findById(1)).thenReturn(Optional.of(group));
		when(groupRepository.save(any(Group.class))).thenReturn(group);

		Group updatedGroup = groupService.updateGroupType(1, true);

		assertTrue(updatedGroup.getIsPrivate());
		verify(groupRepository, times(1)).findById(1);
		verify(groupRepository, times(1)).save(any(Group.class));
	}

	@Test
	void testGetMyGroups() {
		List<GroupMember> members = new ArrayList<>();
		GroupMember member = new GroupMember();
		member.setGroup(group);
		member.setUser(user);
		members.add(member);
		when(groupMemberRepository.findByIdUserId(1)).thenReturn(members);

		List<Group> myGroups = groupService.getMyGroups(1);

		assertEquals(1, myGroups.size());
		verify(groupMemberRepository, times(1)).findByIdUserId(1);
	}
}