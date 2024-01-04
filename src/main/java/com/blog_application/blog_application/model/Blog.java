package com.blog_application.blog_application.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Blog {
    @Id
    private String id;

    private String title;
    private String content;
    private String authorId; // Reference to the user document ID

    private List<String> commentIds; // List of comment document IDs
    private List<String> tags;
    private List<String> links;

    private String imagePath; // Path to the attached image

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorId() {
        return this.authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public List<String> getCommentIds() {
        return this.commentIds;
    }

    public void setCommentIds(List<String> commentIds) {
        this.commentIds = commentIds;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getLinks() {
        return this.links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", title='" + getTitle() + "'" +
                ", content='" + getContent() + "'" +
                ", authorId='" + getAuthorId() + "'" +
                ", commentIds='" + getCommentIds() + "'" +
                ", tags='" + getTags() + "'" +
                ", links='" + getLinks() + "'" +
                ", imagePath='" + getImagePath() + "'" +
                "}";
    }

    public Blog(String id, String title, String content, String authorId, List<String> commentIds, List<String> tags,
            List<String> links, String imagePath) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.commentIds = commentIds;
        this.tags = tags;
        this.links = links;
        this.imagePath = imagePath;
    }

}
