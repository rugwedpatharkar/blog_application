package com.blog_application.blog_application.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Follow {
    @Id
    private String id;

    private String followerId; // Reference to the user document ID
    private String followingId; // Reference to the user document ID

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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
                " id='" + getId() + "'" +
                ", followerId='" + getFollowerId() + "'" +
                ", followingId='" + getFollowingId() + "'" +
                "}";
    }

    public Follow(String id, String followerId, String followingId) {
        this.id = id;
        this.followerId = followerId;
        this.followingId = followingId;
    }

}
