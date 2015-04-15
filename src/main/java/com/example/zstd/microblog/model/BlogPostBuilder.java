package com.example.zstd.microblog.model;

import java.util.Date;

public class BlogPostBuilder {
    private Long id;
    private String creator;
    private String message;
    private String topics;
    private String mentions;
    private Date created;

    private BlogPostBuilder() {
    }

    public static BlogPostBuilder aBlogPost() {
        return new BlogPostBuilder();
    }

    public BlogPostBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public BlogPostBuilder withCreator(String creator) {
        this.creator = creator;
        return this;
    }

    public BlogPostBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public BlogPostBuilder withTopics(String topics) {
        this.topics = topics;
        return this;
    }

    public BlogPostBuilder withMentions(String mentions) {
        this.mentions = mentions;
        return this;
    }

    public BlogPostBuilder withCreated(Date created) {
        this.created = created;
        return this;
    }

    public BlogPostBuilder but() {
        return aBlogPost().withId(id).withCreator(creator).withMessage(message).withTopics(topics).withMentions(mentions).withCreated(created);
    }

    public BlogPost build() {
        BlogPost blogPost = new BlogPost();
        blogPost.setId(id);
        blogPost.setCreator(creator);
        blogPost.setMessage(message);
        blogPost.setTopics(topics);
        blogPost.setMentions(mentions);
        blogPost.setCreated(created);
        return blogPost;
    }
}
