package com.blog_application.blog_application.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
public class Blog {
    @Id
    private String id;

    private String title;
    private String content;
    private String authorId;

    private List<String> commentIds;
    private List<String> tags;

    private String link1;
    private String link2;
    private String link3;
    private String link4;

    @CreatedDate
    private LocalDateTime creationDate;

    public Blog() {
    }

    public Blog(String id, String title, String content, String authorId, List<String> commentIds, List<String> tags,
            String link1, String link2, String link3, String link4, LocalDateTime creationDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.commentIds = commentIds;
        this.tags = tags;
        this.link1 = link1;
        this.link2 = link2;
        this.link3 = link3;
        this.link4 = link4;
        this.creationDate = creationDate;
    }

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

    public String getLink1() {
        return this.link1;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public String getLink2() {
        return this.link2;
    }

    public void setLink2(String link2) {
        this.link2 = link2;
    }

    public String getLink3() {
        return this.link3;
    }

    public void setLink3(String link3) {
        this.link3 = link3;
    }

    public String getLink4() {
        return this.link4;
    }

    public void setLink4(String link4) {
        this.link4 = link4;
    }

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
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
                ", link1='" + getLink1() + "'" +
                ", link2='" + getLink2() + "'" +
                ", link3='" + getLink3() + "'" +
                ", link4='" + getLink4() + "'" +
                ", creationDate='" + getCreationDate() + "'" +
                "}";
    }

}