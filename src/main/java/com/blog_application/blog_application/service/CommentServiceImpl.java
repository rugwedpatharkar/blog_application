package com.blog_application.blog_application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog_application.blog_application.model.Comment;
import com.blog_application.blog_application.model.User;
import com.blog_application.blog_application.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserService userService;

    @Override
    public Comment addComment(String comments, String userId, String blogId) {
        Comment comment = new Comment();
        comment.setComment(comments);
        comment.setUserId(userId);
        comment.setBlogId(blogId);
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByBlogId(String blogId) {
        return commentRepository.findByBlogIdOrderByCreationDateTime(blogId);
    }

    @Override
    public List<Comment> getCommentsWithUserDetails(String blogId) {
        List<Comment> comments = commentRepository.findByBlogId(blogId);
        for (Comment comment : comments) {
            User user = userService.getUserById(comment.getUserId());
            comment.setUser(user);
        }
        return comments;
    }

    @Override
    public Comment getCommentById(String commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @Override
    public void deleteComment(String commentId) {
        commentRepository.deleteById(commentId);
    }
}
