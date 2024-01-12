package com.blog_application.blog_application.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Follow {
    @Id
    private String followId;

    private String followerId; // Reference to the user document ID
    private String followingId; // Reference to the user document ID

    public Follow() {
    }

    public Follow(String followId, String followerId, String followingId) {
        this.followId = followId;
        this.followerId = followerId;
        this.followingId = followingId;
    }

    public String getFollowId() {
        return this.followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }

    public String getFollowerId() {
        return this.followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public String getFollowingId() {
        return this.followingId;
    }

    public void setFollowingId(String followingId) {
        this.followingId = followingId;
    }

    @Override
    public String toString() {
        return "{" +
                " followId='" + getFollowId() + "'" +
                ", followerId='" + getFollowerId() + "'" +
                ", followingId='" + getFollowingId() + "'" +
                "}";
    }

}