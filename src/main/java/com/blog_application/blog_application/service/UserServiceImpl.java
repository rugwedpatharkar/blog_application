package com.blog_application.blog_application.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blog_application.blog_application.exception.AlertException;
import com.blog_application.blog_application.model.Blog;
import com.blog_application.blog_application.model.Follow;
import com.blog_application.blog_application.model.User;
import com.blog_application.blog_application.repository.BlogRepository;
import com.blog_application.blog_application.repository.FollowRepository;
import com.blog_application.blog_application.repository.UserRepository;
import java.util.function.Function;

import io.micrometer.common.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private FollowRepository followRepository;

	@Autowired
	private BlogRepository blogRepository;

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
				throw new AlertException("error", "Invalid password. Please choose a stronger password.");
			}

			user.setPassword(passwordEncoder.encode(user.getPassword()));
			return userRepository.save(user);
		} catch (Exception e) {
			logger.error("Error during user registration", e);
			throw e;
		}
	}

	public List<Blog> getBlogsByUserId(String userId) {
		// Assuming your Blog entity has an 'authorId' field
		return blogRepository.findByAuthorId(userId);
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

	@Override
	public User updateUserProfile(String userId, User updatedUser) {
		try {
			Optional<User> optionalUser = userRepository.findById(userId);
			if (optionalUser.isPresent()) {
				User existingUser = optionalUser.get();

				// Check if a new password is provided
				if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
					// Validate the new password
					if (!PasswordValidator.isValidPassword(updatedUser.getPassword())) {
						throw new AlertException("note", "Invalid password. Please choose a stronger password.");
					}

					// Encode and set the new password
					String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
					existingUser.setPassword(encodedPassword);
				}

				// Check for uniqueness only if username or email is updated
				if (!existingUser.getUsername().equals(updatedUser.getUsername())
						&& userRepository.findByUsername(updatedUser.getUsername()) != null) {
					throw new AlertException("error", "Username already exists");
				}

				if (!existingUser.getEmail().equals(updatedUser.getEmail())
						&& userRepository.findByEmail(updatedUser.getEmail()) != null) {
					throw new AlertException("error", "Email already exists");
				}

				// Update other fields
				existingUser.setName(updatedUser.getName());
				existingUser.setUsername(updatedUser.getUsername());
				existingUser.setEmail(updatedUser.getEmail());

				// Add other fields as needed

				return userRepository.save(existingUser);
			} else {
				throw new AlertException("warning", "User not found");
			}
		} catch (Exception e) {
			logger.error("Error during user profile update", e);
			throw e;
		}
	}

	@Override
	public User getUserById(String userId) {
		return userRepository.findById(userId).orElse(null);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public List<User> getFollowers(String userId) {
		List<Follow> followers = followRepository.findByFollowingId(userId);
		return followers.stream()
				.map(follow -> getUserById(follow.getFollowerId()))
				.collect(Collectors.toList());
	}

	@Override
	public List<User> getFollowing(String userId) {
		List<Follow> following = followRepository.findByFollowerId(userId);
		return following.stream()
				.map(follow -> getUserById(follow.getFollowingId()))
				.collect(Collectors.toList());
	}

	// UserServiceImpl.java

	@Override
	public void followUser(String followerId, String followingId) {
		// Check if the follow relationship already exists
		if (!followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
			Follow follow = new Follow();
			follow.setFollowerId(followerId);
			follow.setFollowingId(followingId);
			followRepository.save(follow);

			// Update user's follower and following lists
			updateUserFollowLists(followerId, followingId);
		}
	}

	private void updateUserFollowLists(String followerId, String followingId) {
		// Update follower list of the user being followed
		User followingUser = getUserById(followingId);
		followingUser.getFollowerIds().add(followerId);
		userRepository.save(followingUser);

		// Update following list of the follower
		User followerUser = getUserById(followerId);
		followerUser.getFollowingIds().add(followingId);
		userRepository.save(followerUser);
	}

	@Override
	public void unfollowUser(String followerId, String followingId) {
		followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
	}

	@Override
	public boolean isFollowing(String followerId, String followingId) {
		return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
	}

	@Override
	public List<Blog> getBlogsOfFollowingUsers(String userId) {
		List<Follow> follows = followRepository.findByFollowerId(userId);
		List<String> followingIds = follows.stream().map(Follow::getFollowingId).collect(Collectors.toList());

		List<Blog> blogs = blogRepository.findByAuthorIdIn(followingIds);

		// Retrieve authors for the blogs
		Map<String, User> authorMap = userRepository.findByUserIdIn(followingIds)
				.stream()
				.collect(Collectors.toMap(User::getUserId, Function.identity()));

		// Set the username for each blog
		blogs.forEach(blog -> {
			User author = authorMap.get(blog.getAuthorId());
			if (author != null) {
				blog.setUsername(author.getUsername());
				blog.setUserId(author.getUserId());

			}
		});

		return blogs;
	}

}