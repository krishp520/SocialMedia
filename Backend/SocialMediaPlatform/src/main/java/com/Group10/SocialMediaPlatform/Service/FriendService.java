package com.Group10.SocialMediaPlatform.Service;

import com.Group10.SocialMediaPlatform.Repository.UserRepository;
import com.Group10.SocialMediaPlatform.model.Friend;
import com.Group10.SocialMediaPlatform.model.FriendId;
import com.Group10.SocialMediaPlatform.Repository.FriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class FriendService {
    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    public Friend addFriend(Friend friend) {
        validateFriendId(friend);
        validateUserAndFriend(friend);
        return friendRepository.save(friend);
    }

    private void validateFriendId(Friend friend) {
        validateNotNull(friend.getId(), "FriendId cannot be null");
        validateNotNull(friend.getId().getUserId(), "FriendId's UserId cannot be null");
        validateNotNull(friend.getId().getFriendId(), "FriendId's FriendId cannot be null");
    }

    private void validateUserAndFriend(Friend friend) {
        validateNotNull(friend.getUser(), "User cannot be null");
        validateNotNull(friend.getFriend(), "Friend cannot be null");
    }

    private void validateNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }



    public List<Friend> getFriendsByUserId(Integer userId) {
        return friendRepository.findByUser_UserId(userId);
    }

    public void removeFriend(Integer userId, Integer friendId) {

        friendRepository.deleteById(new FriendId(userId, friendId));

        friendRepository.deleteById(new FriendId(friendId, userId));
    }

    public List<Friend> getAllFriendsForUser(Integer userId) {
        List<Friend> friends = new ArrayList<>(friendRepository.findByUser_UserId(userId));
        friends.addAll(friendRepository.findByFriend_UserId(userId));
        return friends;
    }


}


