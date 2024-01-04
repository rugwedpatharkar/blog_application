package com.blog_application.blog_application.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.blog_application.blog_application.model.Follow;

public interface FollowRepository extends MongoRepository<Follow, String> {
    long countByFollowingId(String id);

    long countByFollowerId(String id);
}
