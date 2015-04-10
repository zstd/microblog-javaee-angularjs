package com.example.zstd.microblog.servlet.rest;

import com.example.zstd.microblog.model.BlogPost;
import com.example.zstd.microblog.service.BlogPostService;
import com.example.zstd.microblog.service.ServiceLocator;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

public class PostsServletTest extends ServletTestBase {

    private BlogPostService postService;
    private PostsServlet postsServlet;

    public static final String USER = "USER";
    public static Date BASE_DATE;

    @Override
    public void doSetUp() {
        try {
            BASE_DATE =  new SimpleDateFormat("yyyy.MM.dd").parse("2012.12.12.");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        postService = mock(BlogPostService.class);
        ServiceLocator.initialize(ImmutableMap.<Class, Object>of(
                BlogPostService.class, postService
        ));
        postsServlet = new PostsServlet();
    }

    @Test
    public void testGet() throws Exception {
        givenRequestContainsParameters(ImmutableMap.of(
                PostsServlet.PARAM_CREATOR_NAME, new String[]{USER}
        ));
        givenListForUser(blogPost(1), blogPost(2));

        postsServlet.doGet(request,response);

        assertThat(stringWriter.toString(), sameJSONAs(fileContentAsString("post-list.json")).allowingAnyArrayOrdering());
    }

    private BlogPost blogPost(int index) {
        BlogPost blogPost = new BlogPost();
        blogPost.setTopics("topic-" + index);
        blogPost.setMentions("mention-" + index);
        blogPost.setId(Long.valueOf(index));
        blogPost.setMessage("Some message at index #" + index);
        blogPost.setCreator(USER);
        blogPost.setCreated(new Date(BASE_DATE.getTime() + index));
        return blogPost;
    }

    private void givenListForUser(BlogPost ...blogPosts) {
        when(postService.getListForUser(eq(USER))).thenReturn(Arrays.asList(blogPosts));
    }
}