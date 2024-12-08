package com.Group10.SocialMediaPlatform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Friends")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friend {
    @EmbeddedId
    private FriendId id;

    private String status;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @MapsId("friendId")
    @JoinColumn(name = "friend_id", insertable = false, updatable = false)
    private User friend;
}
