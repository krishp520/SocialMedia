package com.Group10.SocialMediaPlatform.Service;

import com.Group10.SocialMediaPlatform.model.Post;
import com.Group10.SocialMediaPlatform.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post getPostById(Integer postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }
}
