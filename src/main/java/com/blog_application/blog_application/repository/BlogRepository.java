package com.blog_application.blog_application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.blog_application.blog_application.model.Blog;
import com.blog_application.blog_application.model.Tag;

public interface BlogRepository extends MongoRepository<Blog, String> {
    List<Blog> findByAuthorId(String authorId);

    List<Blog> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);

    List<Blog> findByTagsContains(String tag);

    List<Blog> findByAuthorIdIn(List<String> authorIds);

    Optional<Blog> findByBlogIdAndAuthorId(String blogId, String authorId);

    List<Blog> findByTags(Tag tag);

}
