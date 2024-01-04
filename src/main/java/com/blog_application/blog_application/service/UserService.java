package com.blog_application.blog_application.service;

import com.blog_application.blog_application.dto.UserProfileDTO;
import com.blog_application.blog_application.exception.AlertException;
import com.blog_application.blog_application.model.User;

public interface UserService {
	User loginUser(String username, String password) throws AlertException;

	User registerUser(User user);

	User updateUserProfile(String id, User updatedUser);

	void generateResetToken(String email);

	boolean verifyResetToken(String email, String token);

	UserProfileDTO getUserProfile(String userId);

	void resetPassword(String email, String password);
}