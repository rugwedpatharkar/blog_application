package com.blog_application.blog_application.service;

import java.util.List;

import com.blog_application.blog_application.dto.UserProfileDTO;
import com.blog_application.blog_application.exception.AlertException;
import com.blog_application.blog_application.model.Blog;
import com.blog_application.blog_application.model.User;

public interface UserService {
	User loginUser(String username, String password) throws AlertException;

	User registerUser(User user);

	User updateUserProfile(String id, User user);

	void generateResetToken(String email);

	boolean verifyResetToken(String email, String token);

	UserProfileDTO getUserProfile(String userId);

	void resetPassword(String email, String password);

	List<Blog> getBlogsByUserId(String userId);

}