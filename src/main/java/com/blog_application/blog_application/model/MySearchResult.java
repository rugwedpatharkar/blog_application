package com.blog_application.blog_application.model;

import java.util.List;

public class MySearchResult {
    private List<User> matchingUsers;
    private List<Blog> matchingBlogs;
    private List<Blog> matchingBlogsByTag;

    public MySearchResult() {
    }

    public MySearchResult(List<User> matchingUsers, List<Blog> matchingBlogs, List<Blog> matchingBlogsByTag) {
        this.matchingUsers = matchingUsers;
        this.matchingBlogs = matchingBlogs;
        this.matchingBlogsByTag = matchingBlogsByTag;
    }

    public List<User> getMatchingUsers() {
        return this.matchingUsers;
    }

    public void setMatchingUsers(List<User> matchingUsers) {
        this.matchingUsers = matchingUsers;
    }

    public List<Blog> getMatchingBlogs() {
        return this.matchingBlogs;
    }

    public void setMatchingBlogs(List<Blog> matchingBlogs) {
        this.matchingBlogs = matchingBlogs;
    }

    public List<Blog> getMatchingBlogsByTag() {
        return this.matchingBlogsByTag;
    }

    public void setMatchingBlogsByTag(List<Blog> matchingBlogsByTag) {
        this.matchingBlogsByTag = matchingBlogsByTag;
    }

    @Override
    public String toString() {
        return "{" +
                " matchingUsers='" + getMatchingUsers() + "'" +
                ", matchingBlogs='" + getMatchingBlogs() + "'" +
                ", matchingBlogsByTag='" + getMatchingBlogsByTag() + "'" +
                "}";
    }

}
