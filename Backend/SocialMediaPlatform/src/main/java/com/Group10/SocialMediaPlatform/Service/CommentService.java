package com.Group10.SocialMediaPlatform.Service;

import com.Group10.SocialMediaPlatform.model.Comment;
import com.Group10.SocialMediaPlatform.Repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment getCommentById(Integer commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }
}


