package com.example.zstd.microblog.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BlogPostTest {

    @Test
    public void testGetRepublishCount() throws Exception {
        BlogPost post = BlogPostBuilder.aBlogPost().withMessage("RE:RE:RE: Do agree!").build();
        assertEquals(post.getRepublishCount(),3);
    }

    @Test
    public void testCreatePriorityComparator() throws Exception {
        
    }
}