package com.blog_application.blog_application.service;

import java.util.List;

import com.blog_application.blog_application.exception.AlertException;
import com.blog_application.blog_application.model.Blog;
import com.blog_application.blog_application.model.User;

public interface UserService {
	User loginUser(String username, String password) throws AlertException;

	User registerUser(User user);

	User updateUserProfile(String userId, User user);

	void generateResetToken(String email);

	boolean verifyResetToken(String email, String token);

	void resetPassword(String email, String password);

	List<Blog> getBlogsByUserId(String userId);

	User getUserById(String userId);

	List<User> getAllUsers();

	List<User> getFollowers(String userId);

	List<User> getFollowing(String userId);

	void followUser(String followerId, String followingId);

	void unfollowUser(String followerId, String followingId);

	boolean isFollowing(String followerId, String followingId);

	List<Blog> getBlogsOfFollowingUsers(String userId);

}