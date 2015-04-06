package com.example.zstd.microblog.servlet.rest;

import com.example.zstd.microblog.model.User;
import com.example.zstd.microblog.model.UserBuilder;
import com.example.zstd.microblog.repository.UserRepo;
import com.example.zstd.microblog.service.ServiceLocator;
import com.example.zstd.microblog.utils.ServletUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

public class UsersServletTest {

    private static final Logger LOG = Logger.getLogger(UsersServletTest.class.getName());

    private UserRepo userRepo;

    private UsersServlet usersServlet;

    private HttpServletRequest request;
    private HttpServletResponse response;

    private StringWriter stringWriter = new StringWriter();
    private int responseStatusCode = 0;

    @Before
    public void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                responseStatusCode = (Integer) invocation.getArguments()[0];
                return null;
            }
        }).when(response).setStatus(anyInt());

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
        User user = givenUserExists(UserBuilder.anUser().
                withNames("username", "nickname").withPassword("pass"));
        givenRequestContainsParameters(ImmutableMap.of(
                UsersServlet.PARAM_ACTION, new String[]{UsersServlet.Action.CURRENT.toString()}
        ));
        givenRequestContainsUserWithRoles(user.getUsername(),"role1","role2");

        usersServlet.doGet(request,response);

        assertThat(stringWriter.toString(), sameJSONAs(fileContentAsString("current-user.json")));
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

    private String fileContentAsString(String pathToFile) {
        Preconditions.checkNotNull(pathToFile);
        String result = null;
        try {
            URL url = this.getClass().getResource(pathToFile);
            Preconditions.checkNotNull(url,"Resource not found: " + pathToFile);
            String fileStr = this.getClass().getResource(pathToFile).getFile();
            LOG.log(Level.CONFIG,"Loading resource from file: " + fileStr);
            result = Files.toString(new File(fileStr), Charset.forName("UTF-8"));
        } catch (IOException e) {
            Throwables.propagate(e);
        }
        return result;
    }

    @Test
    public void testGetWithUserInfoAction() throws Exception {
        User user = givenUserExists(UserBuilder.anUser().
                withNames("username","nickname").withPassword("pass").withPhotoUrl("/some/photo.jpeg"));
        givenRequestContainsParameters(ImmutableMap.of(
                UsersServlet.PARAM_ACTION, new String[]{UsersServlet.Action.USER_INFO.toString()},
                UsersServlet.PARAM_USER, new String[]{user.getUsername()}
        ));

        usersServlet.doGet(request,response);

        assertThat(stringWriter.toString(), sameJSONAs(fileContentAsString("user-with-photo.json")));
    }

    @Test
    public void testPostNotAllowed() throws Exception {
        usersServlet.doPost(request,response);
        assertEquals(responseStatusCode, ServletUtils.METHOD_NOT_ALLOWED);

    }
}