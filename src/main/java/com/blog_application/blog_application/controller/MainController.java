package com.blog_application.blog_application.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blog_application.blog_application.exception.AlertException;
import com.blog_application.blog_application.model.Alert;
import com.blog_application.blog_application.model.Blog;
import com.blog_application.blog_application.model.Comment;
import com.blog_application.blog_application.model.User;
import com.blog_application.blog_application.model.MySearchResult;
import com.blog_application.blog_application.service.BlogService;
import com.blog_application.blog_application.service.CommentService;
import com.blog_application.blog_application.service.SearchService;
import com.blog_application.blog_application.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@Autowired
	private CommentService commentService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private UserService userService;
	@Autowired
	private BlogService blogService;

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
			List<Blog> followingBlogs = userService.getBlogsOfFollowingUsers(loggedInUser.getUserId());
			model.addAttribute("followingBlogs", followingBlogs);
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
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			User userProfile = userService.getUserById(user.getUserId());
			model.addAttribute("user", userProfile);

			List<Blog> followingBlogs = userService.getBlogsOfFollowingUsers(userProfile.getUserId());
			model.addAttribute("followingBlogs", followingBlogs);

			return "home";
		} else {
			model.addAttribute("alert", new Alert("error", "Something went wrong, Please Login again."));
			return "redirect:/";
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

	@GetMapping("/profile")
	public String viewProfile(Model model, HttpSession session) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			User userProfile = userService.getUserById(user.getUserId());
			List<Blog> userBlogs = userService.getBlogsByUserId(user.getUserId());
			model.addAttribute("user", userProfile);
			model.addAttribute("blogs", userBlogs);
			return "profile";
		} else {
			model.addAttribute("alert", new Alert("error", "Something went wrong, Please Login again."));
			return "redirect:/";
		}
	}

	@GetMapping("/update-profile")
	public String showUpdateProfilePage(Model model, HttpSession session) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			User userProfile = userService.getUserById(user.getUserId());
			model.addAttribute("user", userProfile);
			return "update-profile";
		} else {
			model.addAttribute("alert", new Alert("error", "Something went wrong, Please Login again."));
			return "redirect:/";
		}
	}

	@PostMapping("/update")
	public String updateProfile(@ModelAttribute User user, Model model, HttpSession session) {
		if (session.getAttribute("user") != null) {
			try {
				User loggedinuser = (User) session.getAttribute("user");
				userService.updateUserProfile(user.getUserId(), loggedinuser);
				return "redirect:/profile";
			} catch (AlertException e) {
				model.addAttribute("alertType", e.getAlertType());
				model.addAttribute("alertMessage", e.getMessage());
				return "update-profile";
			}
		} else {
			model.addAttribute("alert", new Alert("error", "Something went wrong, Please Login again."));
			return "redirect:/";
		}
	}

	@GetMapping("/post-blog")
	public String getPostBlogPage(Model model, HttpSession session) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			model.addAttribute("user", user);
			return "post-blog";
		} else {
			model.addAttribute("alert", new Alert("error", "Something went wrong, Please Login again."));
			return "redirect:/";
		}
	}

	@PostMapping("/post-blog")
	public String postBlog(@RequestParam("title") String title,
			@RequestParam("content") String content,
			@RequestParam(value = "selectedTags", required = false) String[] selectedTags,
			@RequestParam(value = "link1", required = false) String link1,
			@RequestParam(value = "link2", required = false) String link2,
			@RequestParam(value = "link3", required = false) String link3,
			@RequestParam(value = "link4", required = false) String link4,
			HttpSession session, Model model) {
		if (session.getAttribute("user") != null) {
			User loggedInUser = (User) session.getAttribute("user");
			String userId = loggedInUser.getUserId();
			Blog createdBlog = blogService.createBlog(userId, title, content, selectedTags, link1, link2, link3, link4);
			return "redirect:/blog/" + createdBlog.getBlogId() + "/user/" + userId;
		} else {
			model.addAttribute("alert", new Alert("error", "Something went wrong, Please Login again."));
			return "redirect:/";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@PostMapping("/update-blog/{blogId}")
	public String updateBlog(@PathVariable String blogId, Model model, HttpSession session) {
		if (session.getAttribute("user") != null) {
			Blog blog = blogService.getBlog(blogId);
			User user = (User) session.getAttribute("user");
			User userProfile = userService.getUserById(user.getUserId());
			model.addAttribute("user", userProfile);
			model.addAttribute("blog", blog);
			return "update-blog";
		} else {
			model.addAttribute("alert", new Alert("error", "Something went wrong, Please Login again."));
			return "redirect:/";
		}
	}

	@PostMapping("/update-blog/submit")
	public String updateBlogSubmit(@ModelAttribute Blog updatedBlog, HttpSession session, Model model) {
		if (session.getAttribute("user") != null) {
			blogService.updateBlog(
					updatedBlog.getBlogId(),
					updatedBlog.getTitle(),
					updatedBlog.getContent(),
					updatedBlog.getLink1(),
					updatedBlog.getLink2(),
					updatedBlog.getLink3(),
					updatedBlog.getLink4());
			return "redirect:/profile";
		} else {
			model.addAttribute("alert", new Alert("error", "Something went wrong, Please Login again."));
			return "redirect:/";
		}
	}

	@PostMapping("/delete-blog/{blogId}")
	public String deleteBlog(@PathVariable String blogId, HttpSession session, Model model) {
		if (session.getAttribute("user") != null) {
			blogService.deleteBlog(blogId);
			return "redirect:/profile";
		} else {
			model.addAttribute("alert", new Alert("error", "Something went wrong, Please Login again."));
			return "redirect:/";
		}
	}

	@GetMapping("/search")
	public String search(@RequestParam(name = "query") String query, Model model, HttpSession session) {
		if (session.getAttribute("user") != null) {
			List<User> matchingUsers = searchService.searchUsers(query);
			List<Blog> matchingBlogs = searchService.searchBlogs(query);
			List<Blog> matchingBlogsByTag = searchService.searchBlogsByTag(query);
			MySearchResult searchResult = new MySearchResult(matchingUsers, matchingBlogs, matchingBlogsByTag);
			model.addAttribute("searchResult", searchResult);
			User user = (User) session.getAttribute("user");
			model.addAttribute("user", user);
			return "search-results";
		} else {
			model.addAttribute("alert", new Alert("error", "Something went wrong, Please Login again."));
			return "redirect:/";
		}
	}

	@GetMapping("/followers/{userId}")
	public String getFollowers(@PathVariable String userId, Model model, HttpSession session) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			User userProfile = userService.getUserById(user.getUserId());
			model.addAttribute("user", userProfile);
			List<User> followers = userService.getFollowers(userId);
			model.addAttribute("followers", followers);
			return "followerfollowing";
		} else {
			model.addAttribute("alert", new Alert("error", "Something went wrong, Please Login again."));
			return "redirect:/";
		}
	}

	@GetMapping("/following/{userId}")
	public String getFollowing(@PathVariable String userId, Model model, HttpSession session) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			User userProfile = userService.getUserById(user.getUserId());
			model.addAttribute("user", userProfile);
			List<User> following = userService.getFollowing(userId);
			model.addAttribute("following", following);
			return "followerfollowing";
		} else {
			model.addAttribute("alert", new Alert("error", "Something went wrong, Please Login again."));
			return "redirect:/";
		}
	}

	@GetMapping("/userprofile/{userId}")
	public String viewUserProfile(@PathVariable String userId, Model model, HttpSession session) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			model.addAttribute("user", user);

			User searcheduser = userService.getUserById(userId);
			model.addAttribute("searcheduser", searcheduser);

			List<Blog> userBlogs = userService.getBlogsByUserId(userId);
			model.addAttribute("userBlogs", userBlogs);

			boolean isFollowing = userService.isFollowing(user.getUserId(), userId);
			model.addAttribute("isFollowing", isFollowing);
			return "userprofile";
		} else {
			model.addAttribute("alert", new Alert("error", "Something went wrong, Please Login again."));
			return "redirect:/";
		}
	}

	@PostMapping("/follow")
	public String followUser(@RequestParam String userId, Model model, HttpSession session) {
		if (session.getAttribute("user") != null) {
			User loggedInUser = (User) session.getAttribute("user");
			model.addAttribute("user", userService.getUserById(loggedInUser.getUserId()));

			User searchedUser = userService.getUserById(userId);
			model.addAttribute("searcheduser", searchedUser);

			List<Blog> userBlogs = userService.getBlogsByUserId(userId);
			model.addAttribute("userBlogs", userBlogs);

			boolean isFollowing = userService.isFollowing(loggedInUser.getUserId(), userId);

			if (!isFollowing) {
				userService.followUser(loggedInUser.getUserId(), userId);
				model.addAttribute("message", "Successfully followed");
			} else {
				model.addAttribute("message", "You are already following this user");
			}

			return "redirect:/userprofile/" + userId;
		} else {
			model.addAttribute("alert", new Alert("error", "Something went wrong, Please Login again."));
			return "redirect:/";
		}
	}

	@PostMapping("/unfollow")
	public String unfollowUser(@RequestParam String userId, Model model, HttpSession session) {
		if (session.getAttribute("user") != null) {
			User loggedInUser = (User) session.getAttribute("user");
			model.addAttribute("user", userService.getUserById(loggedInUser.getUserId()));

			User searchedUser = userService.getUserById(userId);
			model.addAttribute("searcheduser", searchedUser);

			List<Blog> userBlogs = userService.getBlogsByUserId(userId);
			model.addAttribute("userBlogs", userBlogs);

			userService.unfollowUser(loggedInUser.getUserId(), userId);
			model.addAttribute("message", "Successfully unfollowed");

			return "redirect:/userprofile/" + userId;
		} else {
			model.addAttribute("alert", new Alert("error", "Something went wrong, Please Login again."));
			return "redirect:/";
		}
	}

	@GetMapping("/blog/{blogId}/user/{userId}")
	public String showBlogDetails(@PathVariable String blogId, @PathVariable String userId, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		if (session.getAttribute("user") != null) {
			User loggedInUser = (User) session.getAttribute("user");
			model.addAttribute("user", userService.getUserById(loggedInUser.getUserId()));

			Optional<Blog> blogOptional = blogService.getBlogDetails(blogId, userId);
			List<Comment> comments = commentService.getCommentsWithUserDetails(blogId);

			if (blogOptional.isPresent()) {
				Blog blog = blogOptional.get();
				User blogUser = userService.getUserById(blog.getAuthorId());
				blog.setUserId(blogUser.getUserId());
				blog.setUsername(blogUser.getUsername());

				model.addAttribute("blog", blog);
				model.addAttribute("comments", comments);
				return "blog";
			} else {
				model.addAttribute("alert", new Alert("error", "Blog not found."));
				return "redirect:/";
			}
		} else {
			model.addAttribute("alert", new Alert("error", "Something went wrong, Please Login again."));
			return "redirect:/";
		}
	}

	@PostMapping("/add-comment")
	public String addComment(
			@RequestParam String comment,
			@RequestParam String blogId,
			@RequestParam String bloguserId,
			@RequestParam String userId,
			Model model,
			HttpSession session) {
		User loggedInUser = (User) session.getAttribute("user");

		if (loggedInUser != null) {
			commentService.addComment(comment, userId, blogId);

			return "redirect:/blog/" + blogId + "/user/" + bloguserId;
		} else {
			model.addAttribute("alert", new Alert("error", "User not authenticated. Please Login."));
			return "redirect:/";
		}
	}

	@PostMapping("/delete-comment")
	public String deleteComment(
			@RequestParam String commentId,
			@RequestParam String blogId,
			@RequestParam String bloguserId,
			@RequestParam String userId,
			Model model,
			HttpSession session) {
		User loggedInUser = (User) session.getAttribute("user");

		if (loggedInUser != null) {
			Comment comment = commentService.getCommentById(commentId);

			if (comment != null && comment.getUserId().equals(loggedInUser.getUserId())) {
				commentService.deleteComment(commentId);
				model.addAttribute("alert", new Alert("success", "Comment deleted successfully."));
			} else {
				model.addAttribute("alert", new Alert("error", "You can only delete your own comments."));
			}

			return "redirect:/blog/" + blogId + "/user/" + bloguserId;
		} else {
			model.addAttribute("alert", new Alert("error", "User not authenticated. Please Login."));
			return "redirect:/";
		}
	}

}