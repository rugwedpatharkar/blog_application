package com.blog_application.blog_application.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.blog_application.blog_application.model.Follow;

public interface FollowRepository extends MongoRepository<Follow, String> {
    long countByFollowingId(String FollowId);

    long countByFollowerId(String FollowId);

    List<Follow> findByFollowerId(String followerId);

    List<Follow> findByFollowingId(String followingId);

    void deleteByFollowerIdAndFollowingId(String followerId, String followingId);

    boolean existsByFollowerIdAndFollowingId(String followerId, String followingId);

}
