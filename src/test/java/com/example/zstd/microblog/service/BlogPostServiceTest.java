package com.example.zstd.microblog.service;

import com.example.zstd.microblog.conf.AppConfig;
import com.example.zstd.microblog.model.BlogPost;
import com.example.zstd.microblog.model.BlogPostBuilder;
import com.example.zstd.microblog.repository.BlogPostRepo;
import com.google.common.collect.ImmutableMap;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.zstd.microblog.model.BlogPostBuilder.aBlogPost;
import static junit.framework.Assert.assertEquals;

public class BlogPostServiceTest {

    private BlogPostService blogPostService;
    private BlogPostRepo blogPostRepo = Mockito.mock(BlogPostRepo.class);

    public static final String USER = "post-user";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUpClass() throws Exception {

        ServiceLocator.initialize(ImmutableMap.<Class, Object>of(
                AppConfig.class,AppConfig.createAppConfig(),
                BlogPostRepo.class, blogPostRepo
        ));
        blogPostService = new BlogPostService();
    }

    @Test
    public void testGetBlogPostByCreator() throws Exception {
        givenBlogPosts(
                aBlogPost().withCreator("another-"+USER),
                aBlogPost().withCreator(USER)
        );

        List<BlogPost> posts = blogPostService.getListForUser(USER);
        assertEquals(posts.size(),1);
    }

    private List<BlogPost> givenBlogPosts(BlogPostBuilder...posts) {
        List<BlogPost> postList = ((List<BlogPostBuilder>)Arrays.asList(posts)).stream()
                .map(BlogPostBuilder::build)
                .collect(Collectors.toList());
        Mockito.when(blogPostRepo.listAll()).thenReturn(postList);
        return postList;
    }
}