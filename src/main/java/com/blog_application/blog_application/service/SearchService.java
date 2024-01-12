package com.blog_application.blog_application.service;

import java.util.List;

import com.blog_application.blog_application.model.Blog;
import com.blog_application.blog_application.model.User;

public interface SearchService {
    List<User> searchUsers(String keyword);

    List<Blog> searchBlogs(String keyword);

    List<Blog> searchBlogsByTag(String tag);
}
