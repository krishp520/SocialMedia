package com.Group10.SocialMediaPlatform.Repository;

import com.Group10.SocialMediaPlatform.model.GroupMember;
import com.Group10.SocialMediaPlatform.model.GroupMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {

    List<GroupMember> findByIdUserId(Integer userId);

    List<GroupMember> findByIdGroupId(Integer groupId);

}
