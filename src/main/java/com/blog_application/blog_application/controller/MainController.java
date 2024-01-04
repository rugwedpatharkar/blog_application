package com.blog_application.blog_application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blog_application.blog_application.dto.UserProfileDTO;
import com.blog_application.blog_application.exception.AlertException;
import com.blog_application.blog_application.model.User;
import com.blog_application.blog_application.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String index() {
		return "login";
	}

	@PostMapping("/login")
	public String loginUser(@RequestParam String username, @RequestParam String password, Model model,
			HttpSession session) {
		try {
			User loggedInUser = userService.loginUser(username, password);
			model.addAttribute("user", loggedInUser);
			session.setAttribute("user", loggedInUser);
			return "home";
		} catch (AlertException e) {
			model.addAttribute("alertType", e.getAlertType());
			model.addAttribute("alertMessage", e.getMessage());
			return "login";
		}
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute User user, Model model) {
		try {
			User registeredUser = userService.registerUser(user);
			model.addAttribute("user", registeredUser);
			return "redirect:/";
		} catch (AlertException e) {
			model.addAttribute("alertType", e.getAlertType());
			model.addAttribute("alertMessage", e.getMessage());
			return "register";
		}
	}

	@GetMapping("/forgot-password")
	public String showForgotPasswordPage() {
		return "/forgot-password";
	}

	@PostMapping("/forgot-password")
	public String processForgotPassword(@RequestParam("email") String email, Model model) {
		try {
			userService.generateResetToken(email);
			model.addAttribute("email", email);
			return "/verify-reset-token";
		} catch (AlertException e) {
			model.addAttribute("alertType", "error");
			model.addAttribute("alertMessage", e.getMessage());
			return "forgot-password";
		}
	}

	@PostMapping("/verify-reset-token")
	public String processVerifyResetToken(@RequestParam("email") String email,
			@RequestParam("token") String token,
			Model model) {
		try {
			userService.verifyResetToken(email, token);
			model.addAttribute("email", email);
			return "/reset-password";
		} catch (AlertException e) {
			model.addAttribute("alertType", "error");
			model.addAttribute("alertMessage", e.getMessage());
			model.addAttribute("email", email);
			return "verify-reset-token";
		}
	}

	@PostMapping("/reset-password")
	public String processResetPassword(@RequestParam("email") String email,
			@RequestParam("password") String password, Model model) {
		try {
			userService.resetPassword(email, password);
			return "/login";
		} catch (AlertException e) {
			model.addAttribute("alertType", "error");
			model.addAttribute("alertMessage", e.getMessage());
			model.addAttribute("email", email);
			return "/reset-password";
		}
	}

	// @GetMapping("/profile/{userId}")
	// public String viewUserProfile(@PathVariable String userId, Model model) {
	// UserProfileDTO userProfile = userService.getUserProfile(userId);
	// model.addAttribute("userProfile", userProfile);
	// return "profile";
	// }
	@GetMapping("/profile/{id}")
	public String viewProfile(@PathVariable String id, Model model) {
		UserProfileDTO userProfile = userService.getUserProfile(id);
		model.addAttribute("user", userProfile);

		return "profile";
	}

	@GetMapping("/update/{id}")
	public String updateProfile(@PathVariable String id, Model model) {
		UserProfileDTO updateProfile = userService.getUserProfile(id);
		model.addAttribute("user", updateProfile);

		return "profile";
	}
}
