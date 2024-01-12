package com.blog_application.blog_application.service;

import java.util.List;

import com.blog_application.blog_application.model.Comment;

public interface CommentService {
    Comment addComment(String comment, String userId, String blogId);

    List<Comment> getCommentsByBlogId(String blogId);

    List<Comment> getCommentsWithUserDetails(String blogId);

    public Comment getCommentById(String commentId);

    void deleteComment(String commentId);

}
