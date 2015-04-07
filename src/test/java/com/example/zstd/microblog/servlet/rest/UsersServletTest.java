package com.example.zstd.microblog.servlet.rest;

import com.example.zstd.microblog.model.User;
import com.example.zstd.microblog.model.UserBuilder;
import com.example.zstd.microblog.repository.UserRepo;
import com.example.zstd.microblog.service.ServiceLocator;
import com.example.zstd.microblog.utils.ServletUtils;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Arrays;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

public class UsersServletTest extends ServletTestBase {

    private static final Logger LOG = Logger.getLogger(UsersServletTest.class.getName());

    private UserRepo userRepo;

    private UsersServlet usersServlet;

    
    @Override
    public void doSetUp() {

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
        when(userRepo.findByField(eq(User.DB_FIELD_USERNAME), eq(user.getUsername()))).
                thenReturn(Arrays.asList(user));
        return user;
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