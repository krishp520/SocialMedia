package com.Group10.SocialMediaPlatform.Repository;

import com.Group10.SocialMediaPlatform.model.Friend;
import com.Group10.SocialMediaPlatform.model.FriendId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendId> {

    List<Friend> findByUser_UserId(Integer userId);

    List<Friend> findByFriend_UserId(Integer friendUserId);

}
