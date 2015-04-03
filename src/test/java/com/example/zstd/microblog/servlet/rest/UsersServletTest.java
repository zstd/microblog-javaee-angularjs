package com.example.zstd.microblog.servlet.rest;

import com.example.zstd.microblog.model.User;
import com.example.zstd.microblog.model.UserBuilder;
import com.example.zstd.microblog.repository.UserRepo;
import com.example.zstd.microblog.service.ServiceLocator;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Principal;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

public class UsersServletTest {

    //private ServletRunner servletRunner = new ServletRunner();

    private UserRepo userRepo;

    private UsersServlet usersServlet;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter = new StringWriter();

    @Before
    public void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        userRepo = mock(UserRepo.class);
        ServiceLocator.initialize(ImmutableMap.<Class, Object>of(
                UserRepo.class, userRepo
        ));
        usersServlet = new UsersServlet();
    }

    @Test
    public void testGetWithUndefinedAction() throws Exception {

        usersServlet.doGet(request,response);

        assertEquals(UsersServlet.UNDEFINED_ACTION_ERROR, stringWriter.toString());
    }

    private void givenRequestContainsParameters(final Map<String,String[]> map) {
        when(request.getParameterMap()).thenReturn(map);
        when(request.getParameter(anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String paramValue[] = map.get(invocation.getArguments()[0]);
                return paramValue != null ? paramValue[0] : null;
            }
        });
    }

    @Test
    public void testGetWithCurrentUserAction() throws Exception {
        User user = givenUserExists(UserBuilder.anUser().withNames("username","nickname").withPassword("pass"));
        givenRequestContainsParameters(ImmutableMap.of(
                UsersServlet.ACTION_PARAM, new String[]{UsersServlet.Action.CURRENT.toString()}
        ));
        givenRequestContainsUserWithRoles(user.getUsername(),"role1","role2");

        usersServlet.doGet(request,response);

        assertThat(stringWriter.toString(),sameJSONAs("{\"username\":\"username\",\"nickname\":\"nickname\",\"photoUrl\":\"/blog/static/img/default.jpg\"}"));
        //assertEquals(UsersServlet.UNDEFINED_ACTION_ERROR, stringWriter.toString());
    }

    private User givenUserExists(UserBuilder userBuilder) {
        User user = userBuilder.build();
        when(userRepo.findByField(eq(User.DB_FIELD_USERNAME),eq(user.getUsername()))).
                thenReturn(Arrays.asList(user));
        return user;
    }

    private void givenRequestContainsUserWithRoles(final String name, final String...roles) {
        when(request.getUserPrincipal()).thenReturn(new Principal() {
            @Override
            public String getName() {
                return name;
            }
        });
        when(request.isUserInRole(anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return Arrays.asList(roles).contains(invocation.getArguments()[0]);
            }
        });
    }
//
//    @Test
//    //@Ignore("need to find out the way to mock getUserPrincipal() method")
//    public void testGetWithUserInfoAction() throws Exception {
//        ServletUnitClient sc = servletRunner.newClient();
//        WebRequest request   = new GetMethodWebRequest( "http://microblog/myServlet" );
//        request.setParameter(UsersServlet.ACTION_PARAM, UsersServlet.Action.USER_INFO.toString());
//        WebResponse response = sc.getResponse( request );
//        assertNotNull("No response received", response );
//        assertEquals( "content type", "text/plain", response.getContentType() );
//        assertEquals(UsersServlet.UNDEFINED_ACTION_ERROR, response.getText() );
//    }
}