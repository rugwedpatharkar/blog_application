package com.blog_application.blog_application.controller;

import java.util.List;

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
import com.blog_application.blog_application.model.Alert;
import com.blog_application.blog_application.model.Blog;
import com.blog_application.blog_application.model.User;
import com.blog_application.blog_application.service.BlogService;
import com.blog_application.blog_application.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@Autowired
	private UserService userService;
	@Autowired
	private BlogService blogService;

	// index page /login page
	@GetMapping("/")
	public String index() {
		return "login";
	}

	// login form
	@PostMapping("/login")
	public String loginUser(@RequestParam String username, @RequestParam String password, Model model,
			HttpSession session) {
		try {
			User loggedInUser = userService.loginUser(username, password);
			model.addAttribute("user", loggedInUser);
			session.setAttribute("user", loggedInUser);
			model.addAttribute("alert", new Alert("success", "Login successful"));
			return "home";
		} catch (AlertException e) {
			model.addAttribute("alertType", e.getAlertType());
			model.addAttribute("alertMessage", e.getMessage());
			return "login";
		}
	}

	@GetMapping("/home")
	public String home(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		UserProfileDTO userProfile = userService.getUserProfile(user.getId());
		model.addAttribute("user", userProfile);
		return "home";
	}

	// register page
	@GetMapping("/register")
	public String register() {
		return "register";
	}

	// register form
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

	// forget password page
	@GetMapping("/forgot-password")
	public String showForgotPasswordPage() {
		return "/forgot-password";
	}

	// accept email and sends OTP to email
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

	@GetMapping("/profile")
	public String viewProfile(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			// Get user profile information
			UserProfileDTO userProfile = userService.getUserProfile(user.getId());

			// Get all blogs posted by the user
			List<Blog> userBlogs = userService.getBlogsByUserId(user.getId());

			// Add user profile and modified blogs to the model
			model.addAttribute("user", userProfile);
			model.addAttribute("blogs", userBlogs);

			return "profile";
		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/update-profile")
	public String showUpdateProfilePage(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");

		if (user != null) {
			UserProfileDTO userProfile = userService.getUserProfile(user.getId());
			model.addAttribute("user", userProfile);
			return "update-profile";
		} else {
			return "redirect:/login";
		}
	}

	@PostMapping("/update")
	public String updateProfile(@ModelAttribute User user, Model model, HttpSession session) {
		User loggedInUser = (User) session.getAttribute("user");

		if (loggedInUser != null) {
			try {
				userService.updateUserProfile(loggedInUser.getId(), user);
				return "redirect:/profile";
			} catch (AlertException e) {
				model.addAttribute("alertType", e.getAlertType());
				model.addAttribute("alertMessage", e.getMessage());
				return "update-profile";
			}
		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/post-blog")
	public String getPostBlogPage(Model model, HttpSession session) {
		if (session.getAttribute("user") != null) {
			User loggedInUser = (User) session.getAttribute("user");
			model.addAttribute("user", loggedInUser);
			return "post-blog";
		} else {
			return "redirect:/login";
		}
	}

	@PostMapping("/post-blog")
	public String postBlog(@RequestParam("title") String title,
			@RequestParam("content") String content,
			@RequestParam(value = "selectedTags", required = false) String[] selectedTags,
			@RequestParam(value = "link1", required = false) String link1,
			@RequestParam(value = "link1", required = false) String link2,
			@RequestParam(value = "link1", required = false) String link3,
			@RequestParam(value = "link1", required = false) String link4,
			HttpSession session) {
		String id = ((User) session.getAttribute("user")).getId();

		Blog createdBlog = blogService.createBlog(id, title, content, selectedTags, link1, link2, link3, link4);

		return "redirect:/blog/" + createdBlog.getId();
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}

	@PostMapping("/update-blog/{blogId}")
	public String updateBlog(@PathVariable String blogId, Model model, HttpSession session) {
		Blog blog = blogService.getBlog(blogId);
		User user = (User) session.getAttribute("user");
		UserProfileDTO userProfile = userService.getUserProfile(user.getId());
		model.addAttribute("user", userProfile);
		model.addAttribute("blog", blog);
		return "update-blog";
	}

	@PostMapping("/update-blog/submit")
	public String updateBlogSubmit(@ModelAttribute Blog updatedBlog) {
		blogService.updateBlog(
				updatedBlog.getId(),
				updatedBlog.getTitle(),
				updatedBlog.getContent(),
				updatedBlog.getLink1(),
				updatedBlog.getLink2(),
				updatedBlog.getLink3(),
				updatedBlog.getLink4());
		return "redirect:/profile";
	}

	@PostMapping("/delete-blog/{blogId}")
	public String deleteBlog(@PathVariable String blogId) {
		blogService.deleteBlog(blogId);
		return "redirect:/profile";
	}
}
