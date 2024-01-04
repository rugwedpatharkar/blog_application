package com.blog_application.blog_application.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.blog_application.blog_application.model.User;

public interface UserRepository extends MongoRepository<User, String> {
	User findByEmail(String email);

	User findByUsername(String username);

	Optional<User> findById(String id);

}