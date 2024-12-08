package com.Group10.SocialMediaPlatform.Repository;

import com.Group10.SocialMediaPlatform.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByUserUserId(Integer userId);

}
