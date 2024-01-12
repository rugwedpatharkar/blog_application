package com.blog_application.blog_application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.blog_application.blog_application.model.User;

public interface UserRepository extends MongoRepository<User, String> {
	User findByEmail(String email);

	User findByUsername(String username);

	Optional<User> findByUserId(String UserId);

	List<User> findByUsernameContaining(String keyword);

	List<User> findByUserIdIn(List<String> userIds);

}