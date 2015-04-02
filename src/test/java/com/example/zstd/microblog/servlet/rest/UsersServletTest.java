package com.example.zstd.microblog.servlet.rest;

import com.example.zstd.microblog.repository.UserRepo;
import com.example.zstd.microblog.service.ServiceLocator;
import com.google.common.collect.ImmutableMap;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class UsersServletTest {

    private ServletRunner servletRunner = new ServletRunner();

    private UserRepo userRepo;

    @Before
    public void setUp() throws Exception {
        servletRunner.registerServlet("myServlet", UsersServlet.class.getName());

        userRepo = mock(UserRepo.class);
        ServiceLocator.initialize(ImmutableMap.<Class, Object>of(
                UserRepo.class, userRepo
        ));
    }

    @Test
    public void testGetWithUndefinedAction() throws Exception {
        ServletUnitClient sc = servletRunner.newClient();
        WebRequest request   = new GetMethodWebRequest( "http://microblog/myServlet" );

        request.setParameter( "color", "red" );
        WebResponse response = sc.getResponse( request );
        assertNotNull("No response received", response );
        assertEquals( "content type", "text/plain", response.getContentType() );
        assertEquals(UsersServlet.UNDEFINED_ACTION_ERROR, response.getText() );

    }

    @Test
    //@Ignore("need to find out the way to mock getUserPrincipal() method")
    public void testGetWithCurrentUserAction() throws Exception {
        ServletUnitClient sc = servletRunner.newClient();
        WebRequest request   = new GetMethodWebRequest( "http://microblog/myServlet" );
        request.setParameter(UsersServlet.ACTION_PARAM, UsersServlet.Action.CURRENT.toString());
        WebResponse response = sc.getResponse( request );
        assertNotNull("No response received", response );
        assertEquals( "content type", "text/plain", response.getContentType() );
        assertEquals(UsersServlet.UNDEFINED_ACTION_ERROR, response.getText() );
    }

    @Test
    //@Ignore("need to find out the way to mock getUserPrincipal() method")
    public void testGetWithUserInfoAction() throws Exception {
        ServletUnitClient sc = servletRunner.newClient();
        WebRequest request   = new GetMethodWebRequest( "http://microblog/myServlet" );
        request.setParameter(UsersServlet.ACTION_PARAM, UsersServlet.Action.USER_INFO.toString());
        WebResponse response = sc.getResponse( request );
        assertNotNull("No response received", response );
        assertEquals( "content type", "text/plain", response.getContentType() );
        assertEquals(UsersServlet.UNDEFINED_ACTION_ERROR, response.getText() );
    }
}