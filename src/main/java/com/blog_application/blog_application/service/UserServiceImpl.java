package com.blog_application.blog_application.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blog_application.blog_application.dto.UserProfileDTO;
import com.blog_application.blog_application.exception.AlertException;
import com.blog_application.blog_application.model.User;
import com.blog_application.blog_application.repository.FollowRepository;
import com.blog_application.blog_application.repository.UserRepository;

import io.micrometer.common.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private FollowRepository followRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String fromEmail;

	@Override
	public User loginUser(String username, String password) {
		User user = userRepository.findByUsername(username);

		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			return user;
		}

		throw new AlertException("warning", "Invalid username or password");
	}

	@Override
	public User registerUser(User user) {
		try {
			if (userRepository.findByUsername(user.getUsername()) != null) {
				throw new AlertException("error", "Username already exists");
			}
			if (userRepository.findByEmail(user.getEmail()) != null) {
				throw new AlertException("error", "Email already exists");
			}

			if (!PasswordValidator.isValidPassword(user.getPassword())) {
				throw new AlertException("note", "Invalid password. Please choose a stronger password.");
			}

			user.setPassword(passwordEncoder.encode(user.getPassword()));
			return userRepository.save(user);
		} catch (Exception e) {
			logger.error("Error during user registration", e);
			throw e;
		}
	}

	@Override
	public void generateResetToken(String email) {
		if (StringUtils.isBlank(email)) {
			throw new AlertException("warning", "Email cannot be empty. Please enter a valid email address.");
		}

		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new AlertException("warning",
					"User not found with the entered email. Please check the email address.");
		}

		String otp = generateRandomOTP();
		user.setResetToken(otp);
		userRepository.save(user);

		sendResetTokenEmail(user.getEmail(), otp);
	}

	@Override
	public boolean verifyResetToken(String email, String token) {
		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new AlertException("warning", "Something went wrong. Please try again.");
		}

		if (StringUtils.isBlank(token)) {
			throw new AlertException("warning", "OTP cannot be empty. Please enter the OTP.");
		}

		if (user.getResetToken() == null || !user.getResetToken().equals(token)) {
			throw new AlertException("warning", "Invalid OTP. Please enter the correct OTP.");
		}

		return true;
	}

	@Override
	public void resetPassword(String email, String password) {
		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new AlertException("warning",
					"User not found with the entered email. Please check the email address.");
		}
		if (!PasswordValidator.isValidPassword(user.getPassword())) {
			throw new AlertException("warning", "Invalid password. Please choose a stronger password.");
		}
		user.setPassword(passwordEncoder.encode(password));
		user.setResetToken(null);
		userRepository.save(user);
	}

	private void sendResetTokenEmail(String email, String otp) {
		String subject = "BlogBuddy Password Reset OTP";
		String message = "Dear BlogBuddy User,\n\n"
				+ "We received a request to reset your password. Your OTP for password reset is: " + otp + "\n\n"
				+ "If you did not request a password reset, please ignore this email. "
				+ "This OTP is valid for a short period of time for security reasons.\n\n"
				+ "Thank you for using BlogBuddy!\n\n"
				+ "Best regards,\n"
				+ "The BlogBuddy Team";

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(fromEmail);
		mailMessage.setTo(email);
		mailMessage.setSubject(subject);
		mailMessage.setText(message);
		javaMailSender.send(mailMessage);
	}

	private String generateRandomOTP() {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		return String.valueOf(otp);
	}

	public UserProfileDTO getUserProfile(String id) {
		User user = userRepository.findById(id).orElse(null);
		if (user != null) {
			long followersCount = followRepository.countByFollowingId(id);
			long followingCount = followRepository.countByFollowerId(id);

			return new UserProfileDTO(user.getId(), user.getUsername(), user.getName(), user.getEmail(),
					user.getFollowerIds(),
					user.getFollowingIds(),

					followersCount,
					followingCount);
		}

		return null; // Handle the case when the user is not found
	}

	public User updateUserProfile(String id, User updatedUser) {
		try {
			Optional<User> optionalUser = userRepository.findById(id);
			if (optionalUser.isPresent()) {
				User existingUser = optionalUser.get();

				// Check if the updated username already exists for another user
				if (!existingUser.getUsername().equals(updatedUser.getUsername())
						&& userRepository.findByUsername(updatedUser.getUsername()) != null) {
					throw new AlertException("error", "Username already exists");
				}

				// Check if the updated email already exists for another user
				if (!existingUser.getEmail().equals(updatedUser.getEmail())
						&& userRepository.findByEmail(updatedUser.getEmail()) != null) {
					throw new AlertException("error", "Email already exists");
				}

				// Update fields you want to allow updating
				existingUser.setName(updatedUser.getName());
				existingUser.setUsername(updatedUser.getUsername());
				existingUser.setEmail(updatedUser.getEmail());

				// Update password if a new password is provided and meets validation criteria
				String newPassword = updatedUser.getPassword();
				if (newPassword != null && !newPassword.isEmpty() && !PasswordValidator.isValidPassword(newPassword)) {
					throw new AlertException("note", "Invalid password. Please choose a stronger password.");
				} else if (newPassword != null && !newPassword.isEmpty()) {
					String encodedPassword = passwordEncoder.encode(newPassword);
					existingUser.setPassword(encodedPassword);
				}

				// Add other fields as needed

				return userRepository.save(existingUser);
			} else {
				throw new AlertException("error", "User not found");
			}
		} catch (Exception e) {
			logger.error("Error during user profile update", e);
			throw e;
		}
	}
}