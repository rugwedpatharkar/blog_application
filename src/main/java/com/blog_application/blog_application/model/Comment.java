package com.blog_application.blog_application.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Comment {
    @Id
    private String id;

    private String content;
    private String userId; // Reference to the user document ID
    private String blogId; // Reference to the blog document ID

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Comment(String id, String content, String userId, String blogId) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.blogId = blogId;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", content='" + getContent() + "'" +
                ", userId='" + getUserId() + "'" +
                ", blogId='" + getBlogId() + "'" +
                "}";
    }

}
