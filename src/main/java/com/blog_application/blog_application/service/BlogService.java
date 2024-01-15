package com.blog_application.blog_application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.blog_application.blog_application.model.Blog;
import com.blog_application.blog_application.model.Tag;

public interface BlogService {

        Blog createBlog(String blogId, String title, String content, String[] selectedTags,
                        String link1, String link2, String link3, String link4,
                        MultipartFile image);

        Blog updateBlog(String blogId, String title, String content, String link1, String link2,
                        String link3, String link4);

        void deleteBlog(String blogId);

        Blog getBlog(String blogId);

        Optional<Blog> getBlogDetails(String blogId, String userId);

        List<Blog> getBlogsByTag(Tag tag);

}
