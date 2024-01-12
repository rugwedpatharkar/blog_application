package com.blog_application.blog_application.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Comment {
    @Id
    private String commentId;

    private String comment;
    private String userId; // Reference to the user document ID
    private String blogId; // Reference to the blog document ID

    @CreatedDate
    private LocalDateTime creationDateTime;
    @Transient
    private User user;

    public Comment() {
    }

    public User getUser() {
        return this.user;
    }

    public Comment(String commentId, String comment, String userId, String blogId, LocalDateTime creationDateTime,
            User user) {
        this.commentId = commentId;
        this.comment = comment;
        this.userId = userId;
        this.blogId = blogId;
        this.creationDateTime = creationDateTime;
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentId() {
        return this.commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBlogId() {
        return this.blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public LocalDateTime getCreationDateTime() {
        return this.creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    @Override
    public String toString() {
        return "{" +
                " commentId='" + getCommentId() + "'" +
                ", comment='" + getComment() + "'" +
                ", userId='" + getUserId() + "'" +
                ", blogId='" + getBlogId() + "'" +
                ", creationDateTime='" + getCreationDateTime() + "'" +
                "}";
    }

}