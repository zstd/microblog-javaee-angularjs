package com.example.zstd.microblog.repository.impl;

import com.example.zstd.microblog.conf.AppConfig;
import com.example.zstd.microblog.model.BlogPost;
import com.example.zstd.microblog.service.ServiceLocator;
import com.google.common.collect.ImmutableMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class BasicJdbcRepoTest {

    private JdbcBlogPostRepo blogPostRepo;

    @Before
    public void setUp() throws Exception {
        ServiceLocator.initialize(ImmutableMap.<Class, Object>of(
                AppConfig.class, AppConfig.createAppConfig()
        ));
        DriverManager.getConnection(ServiceLocator.getInstance().getService(AppConfig.class).
                getStringParam(AppConfig.Param.JDBC_URL));
        blogPostRepo = new JdbcBlogPostRepo();
    }

    @Test
    public void testCreate() throws Exception {
        List<BlogPost> post = blogPostRepo.listAll();
        assertTrue(post.isEmpty());

    }

    @After
    public void tearDown() throws Exception {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch(SQLException se) {
            if (( (se.getErrorCode() == 50000)
                    && ("XJ015".equals(se.getSQLState()) ))) {
                // we got the expected exception
                System.out.println("Derby shut down normally");
                // Note that for single database shutdown, the expected
                // SQL state is "08006", and the error code is 45000.
            } else {
                // if the error code or SQLState is different, we have
                // an unexpected exception (shutdown failed)
                System.err.println("Derby did not shut down normally");
                //printSQLException(se);
            }
        }

    }
}