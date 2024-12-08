package com.Group10.SocialMediaPlatform.Controller;


import com.Group10.SocialMediaPlatform.Repository.UserRepository;
import com.Group10.SocialMediaPlatform.Service.GroupService;
import com.Group10.SocialMediaPlatform.Service.UserService;
import com.Group10.SocialMediaPlatform.model.Group;
import com.Group10.SocialMediaPlatform.model.GroupMember;
import com.Group10.SocialMediaPlatform.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create/{creatorId}")
    public Group createGroup(@RequestBody Group group, @PathVariable Integer creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        group.setCreatedBy(creator);
        return groupService.createGroup(group);
    }

    @PostMapping("/{groupId}/join")
    public String joinPublicGroup(@PathVariable Integer groupId, @RequestParam Integer userId) {
        groupService.joinPublicGroup(groupId, userId);
        return "successfully joined";
    }

    @PutMapping("/{groupId}/updateType")
    public Group updateGroupType(@PathVariable Integer groupId, @RequestBody Map<String, Boolean> request) {
        Boolean isPrivate = request.get("isPrivate");

        return groupService.updateGroupType(groupId, isPrivate);
    }

    @GetMapping("/{groupId}")
    public Group getGroupById(@PathVariable Integer groupId) {
        return groupService.getGroupByGroupId(groupId);
    }

    @GetMapping("/{groupId}/members")
    public List<GroupMember> getGroupMembersByGroupId(@PathVariable Integer groupId) {
        return groupService.getGroupMembersByGroupId(groupId);
    }

    @DeleteMapping("/{groupId}/members/delete/{userId}")
    public String removeMemberFromCurrGroup(@PathVariable Integer groupId, @PathVariable Integer userId) {
        groupService.removeMemberFromCurrGroup(groupId, userId);
        return "succeed deleted";
    }

    @GetMapping("/all")
    public List<Group> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/user/{userId}")
    public List<Group> getMyGroups(@PathVariable Integer userId) {
        return groupService.getMyGroups(userId);
    }

    @PutMapping("/{groupId}/interests")
    public String updateInterests(@PathVariable Integer groupId, @RequestBody Map<String, String> request) {
        String interests = request.get("interests");
        Group updatedGroup = groupService.updateInterests(groupId, interests);
        return updatedGroup.getInterests();
    }
}
