package com.blog_application.blog_application.dto;

import java.util.List;

public class UserProfileDTO {
    private String id;
    private String username;
    private String name;
    private String email;
    private List<String> followerIds;
    private List<String> followingIds;
    private long followersCount;
    private long followingCount;

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", username='" + getUsername() + "'" +
                ", name='" + getName() + "'" +
                ", email='" + getEmail() + "'" +
                ", followerIds='" + getFollowerIds() + "'" +
                ", followingIds='" + getFollowingIds() + "'" +
                ", followersCount='" + getFollowersCount() + "'" +
                ", followingCount='" + getFollowingCount() + "'" +
                "}";
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public long getFollowersCount() {
        return this.followersCount;
    }

    public void setFollowersCount(long followersCount) {
        this.followersCount = followersCount;
    }

    public long getFollowingCount() {
        return this.followingCount;
    }

    public void setFollowingCount(long followingCount) {
        this.followingCount = followingCount;
    }

    public UserProfileDTO(String id, String username, String name, String email, List<String> followerIds,
            List<String> followingIds, long followersCount, long followingCount) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.followerIds = followerIds;
        this.followingIds = followingIds;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }

}
