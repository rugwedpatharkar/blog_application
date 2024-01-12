package com.blog_application.blog_application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog_application.blog_application.model.Blog;
import com.blog_application.blog_application.model.User;
import com.blog_application.blog_application.repository.BlogRepository;
import com.blog_application.blog_application.repository.UserRepository;

// SearchServiceImpl.java
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public List<User> searchUsers(String keyword) {
        return userRepository.findByUsernameContaining(keyword);
    }

    @Override
    public List<Blog> searchBlogs(String keyword) {
        return blogRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }

    @Override
    public List<Blog> searchBlogsByTag(String tag) {
        return blogRepository.findByTagsContains(tag);
    }
}
