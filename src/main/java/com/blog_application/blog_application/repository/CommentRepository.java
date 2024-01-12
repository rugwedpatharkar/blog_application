package com.blog_application.blog_application.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.blog_application.blog_application.model.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByBlogIdOrderByCreationDateTime(String blogId);

    List<Comment> findByBlogId(String blogId);

}
