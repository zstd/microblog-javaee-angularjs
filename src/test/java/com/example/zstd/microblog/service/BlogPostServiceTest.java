package com.example.zstd.microblog.service;

import com.example.zstd.microblog.conf.AppConfig;
import com.example.zstd.microblog.model.BlogPost;
import com.example.zstd.microblog.model.BlogPostBuilder;
import com.example.zstd.microblog.repository.BlogPostRepo;
import com.example.zstd.microblog.repository.UserRepo;
import com.google.common.collect.ImmutableMap;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.zstd.microblog.model.BlogPostBuilder.aBlogPost;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class BlogPostServiceTest {

    private BlogPostService blogPostService;
    private BlogPostRepo blogPostRepo = Mockito.mock(BlogPostRepo.class);
    private UserRepo userRepo = Mockito.mock(UserRepo.class);

    ArgumentCaptor<BlogPost> argument = ArgumentCaptor.forClass(BlogPost.class);

    public static final String USER = "post-user";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUpClass() throws Exception {

        ServiceLocator.initialize(ImmutableMap.<Class, Object>of(
                AppConfig.class,AppConfig.createAppConfig(),
                BlogPostRepo.class, blogPostRepo,
                UserRepo.class,userRepo
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

    @Test
    public void testSaveBlogPostWithMentionOfNotExistingUsername() throws Exception {
        String message = "Message with @mentions and #topic";
        blogPostService.save(USER,message);

        Mockito.verify(blogPostRepo).save(argument.capture());

        assertThat(argument.getValue().getCreator(),is(USER));
        assertThat(argument.getValue().getMessage(),is(message));
        assertThat(argument.getValue().getTopics(),is("topic"));
        assertThat(argument.getValue().getMentions(),nullValue());
    }

    private List<BlogPost> givenBlogPosts(BlogPostBuilder...posts) {
        List<BlogPost> postList = ((List<BlogPostBuilder>)Arrays.asList(posts)).stream()
                .map(BlogPostBuilder::build)
                .collect(Collectors.toList());
        Mockito.when(blogPostRepo.listAll()).thenReturn(postList);
        return postList;
    }
}