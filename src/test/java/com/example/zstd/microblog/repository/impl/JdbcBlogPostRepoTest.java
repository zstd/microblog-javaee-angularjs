package com.example.zstd.microblog.repository.impl;

import com.example.zstd.microblog.model.BlogPost;
import com.example.zstd.microblog.model.User;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static com.example.zstd.microblog.model.UserBuilder.anUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JdbcBlogPostRepoTest extends JdbcRepoTestBase {

    private static final Logger LOG = Logger.getLogger(JdbcRepoTestBase.class.getName());

    private JdbcBlogPostRepo blogPostRepo;

    @Override
    protected void setUpRepos() {
        blogPostRepo = new JdbcBlogPostRepo();
        userRepo = new JdbcUserRepo();
    }

    @Test
    public void testCreateAndList() throws Exception {
        LOG.info("testCreateAndList");
        User user = givenUserExists(anUser().withUsername("alice").withPassword("alice-pass").withNickname("Alicia"));
        List<BlogPost> posts = blogPostRepo.listAll();
        assertTrue(posts.isEmpty());

        BlogPost blogPost = new BlogPost(1L,user.getUsername(),"post data",null,null,new Date());
        blogPostRepo.save(blogPost);

        posts = blogPostRepo.listAll();
        assertEquals(1, posts.size());
    }

}