package com.blog_application.blog_application.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blog_application.blog_application.model.Blog;
import com.blog_application.blog_application.model.Tag;
import com.blog_application.blog_application.model.User;
import com.blog_application.blog_application.repository.BlogRepository;
import com.blog_application.blog_application.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    public BlogServiceImpl(BlogRepository blogRepository, UserRepository userRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Blog createBlog(String blogId, String title, String content, String[] selectedTags,
            String link1, String link2, String link3, String link4,
            MultipartFile image) {
        Optional<User> optionalUser = userRepository.findById(blogId);
        if (optionalUser.isPresent()) {
            User author = optionalUser.get();

            Blog blog = new Blog();
            blog.setTitle(title);
            blog.setContent(content);
            blog.setAuthorId(blogId);

            // Use the selected tags if available, otherwise, set an empty list
            List<String> tagsList = (selectedTags != null) ? List.of(selectedTags) : List.of();
            blog.setTags(tagsList);

            // Set the links
            blog.setLink1(link1);
            blog.setLink2(link2);
            blog.setLink3(link3);
            blog.setLink4(link4);

            // Save the image
            if (image != null && !image.isEmpty()) {
                String imagePath = saveImage(image);
                blog.setImagePath(imagePath);
            }

            // Set the creation date and time
            blog.setCreationDate(LocalDateTime.now());

            Blog savedBlog = blogRepository.save(blog);

            // Update user's blogIds
            author.getBlogIds().add(savedBlog.getBlogId());
            userRepository.save(author);

            return savedBlog;
        } else {
            throw new RuntimeException("User not found");
        }
    }

    private String saveImage(MultipartFile image) {
        try {
            String imagePath = "/images/" + UUID.randomUUID() + "_" + image.getOriginalFilename();
            Files.copy(image.getInputStream(), Paths.get("src/main/resources/static" + imagePath),
                    StandardCopyOption.REPLACE_EXISTING);
            return imagePath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    @Override
    public Blog updateBlog(String blogId, String title, String content, String link1,
            String link2,
            String link3, String link4) {
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        if (optionalBlog.isPresent()) {
            Blog blog = optionalBlog.get();
            blog.setTitle(title);
            blog.setContent(content);

            blog.setLink1(link1);
            blog.setLink2(link2);
            blog.setLink3(link3);
            blog.setLink4(link4);
            // You might want to update the modification date/time here if needed

            return blogRepository.save(blog);
        } else {
            throw new RuntimeException("Blog not found");
        }
    }

    @Override
    public void deleteBlog(String blogId) {
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        if (optionalBlog.isPresent()) {
            Blog blog = optionalBlog.get();
            // Remove blog from author's list of blogs
            User author = userRepository.findById(blog.getAuthorId())
                    .orElseThrow(() -> new RuntimeException("Author not found"));
            author.getBlogIds().remove(blogId);
            userRepository.save(author);

            // Delete the blog
            blogRepository.deleteById(blogId);
        } else {
            throw new RuntimeException("Blog not found");
        }
    }

    @Override
    public Blog getBlog(String blogId) {
        return blogRepository.findById(blogId).orElse(null);
    }

    @Override
    public Optional<Blog> getBlogDetails(String blogId, String userId) {
        return blogRepository.findByBlogIdAndAuthorId(blogId, userId);
    }

    @Override
    public List<Blog> getBlogsByTag(Tag tag) {
        return blogRepository.findByTags(tag);
    }
}