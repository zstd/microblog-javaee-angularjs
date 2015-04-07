package com.example.zstd.microblog.servlet.rest;

import com.example.zstd.microblog.service.FollowDataService;
import com.example.zstd.microblog.service.ServiceLocator;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class FollowDataServletTest extends ServletTestBase {

    private static final Logger LOG = Logger.getLogger(FollowDataServletTest.class.getName());

    private FollowDataService followDataService;

    private FollowDataServlet followDataServlet;

    @Override
    public void doSetUp() {

        followDataService = mock(FollowDataService.class);
        ServiceLocator.initialize(ImmutableMap.<Class, Object>of(
                FollowDataService.class, followDataService
        ));
        followDataServlet = new FollowDataServlet();
    }

    @Test
    public void testGetWithUndefinedAction() throws Exception {

        followDataServlet.doGet(request,response);

        assertEquals("[]", stringWriter.toString());
    }


}