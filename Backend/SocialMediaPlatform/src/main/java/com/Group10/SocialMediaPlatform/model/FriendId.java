package com.Group10.SocialMediaPlatform.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class FriendId implements Serializable {
    private Integer userId;
    private Integer friendId;
}
