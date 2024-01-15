package com.blog_application.blog_application.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        List<Blog> matchingBlogs = blogRepository.findByTitleContainingOrContentContaining(keyword, keyword);

        Map<String, User> authorMap = userRepository
                .findByUserIdIn(matchingBlogs.stream().map(Blog::getAuthorId).collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(User::getUserId, Function.identity()));

        matchingBlogs.forEach(blog -> {
            User author = authorMap.get(blog.getAuthorId());
            if (author != null) {
                blog.setUsername(author.getUsername());
                blog.setUserId(author.getUserId());
            }
        });

        return matchingBlogs;
    }

    @Override
    public List<Blog> searchBlogsByTag(String tag) {
        List<Blog> matchingBlogs = blogRepository.findByTagsContains(tag);

        Map<String, User> authorMap = userRepository
                .findByUserIdIn(matchingBlogs.stream().map(Blog::getAuthorId).collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(User::getUserId, Function.identity()));

        matchingBlogs.forEach(blog -> {
            User author = authorMap.get(blog.getAuthorId());
            if (author != null) {
                blog.setUsername(author.getUsername());
                blog.setUserId(author.getUserId());
            }
        });

        return matchingBlogs;
    }
}
