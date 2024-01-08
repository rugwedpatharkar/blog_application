package com.blog_application.blog_application.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.blog_application.blog_application.model.Blog;

public interface BlogRepository extends MongoRepository<Blog, String> {
    List<Blog> findByAuthorId(String authorId);

}
