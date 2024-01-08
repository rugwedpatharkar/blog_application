package com.blog_application.blog_application.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class User {
	@Id
	private String id;

	private String name;
	private String username;
	private String email;
	private String password;
	private String resetToken;

	private List<String> blogIds = new ArrayList<>();
	private List<String> commentIds = new ArrayList<>();
	private List<String> followerIds = new ArrayList<>();
	private List<String> followingIds = new ArrayList<>();

	public User() {
		this.blogIds = new ArrayList<>();
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getResetToken() {
		return this.resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public List<String> getBlogIds() {
		return this.blogIds;
	}

	public void setBlogIds(List<String> blogIds) {
		this.blogIds = blogIds;
	}

	public List<String> getCommentIds() {
		return this.commentIds;
	}

	public void setCommentIds(List<String> commentIds) {
		this.commentIds = commentIds;
	}

	public List<String> getFollowerIds() {
		return this.followerIds;
	}

	public void setFollowerIds(List<String> followerIds) {
		this.followerIds = followerIds;
	}

	public List<String> getFollowingIds() {
		return this.followingIds;
	}

	public void setFollowingIds(List<String> followingIds) {
		this.followingIds = followingIds;
	}

	public User(String id, String name, String username, String email, String password, String resetToken,
			List<String> blogIds, List<String> commentIds, List<String> followerIds, List<String> followingIds) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
		this.resetToken = resetToken;
		this.blogIds = blogIds;
		this.commentIds = commentIds;
		this.followerIds = followerIds;
		this.followingIds = followingIds;
	}

	@Override
	public String toString() {
		return "{" +
				" id='" + getId() + "'" +
				", name='" + getName() + "'" +
				", username='" + getUsername() + "'" +
				", email='" + getEmail() + "'" +
				", password='" + getPassword() + "'" +
				", resetToken='" + getResetToken() + "'" +
				", blogIds='" + getBlogIds() + "'" +
				", commentIds='" + getCommentIds() + "'" +
				", followerIds='" + getFollowerIds() + "'" +
				", followingIds='" + getFollowingIds() + "'" +
				"}";
	}

}
