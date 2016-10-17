package com.example.zstd.microblog.servlet;

import com.example.zstd.microblog.repository.UserRepo;
import com.example.zstd.microblog.service.ServiceLocator;
import com.example.zstd.microblog.servlet.rest.ServletTestBase;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.logging.Logger;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RouteServletTest extends ServletTestBase {

    private static final Logger LOG = Logger.getLogger(RouteServletTest.class.getName());

    private UserRepo userRepo;

    private RouteServlet routeServlet;


    @Override
    public void doSetUp() {

        userRepo = mock(UserRepo.class);
        ServiceLocator.initialize(ImmutableMap.<Class, Object>of(
                UserRepo.class, userRepo
        ));
        routeServlet = new RouteServlet();
    }

    @Test
    public void testGetNotExistingRoute() throws Exception {

        routeServlet.doGet(request,response);

        assertEquals("", stringWriter.toString());
        assertEquals(404, responseStatusCode);
    }

    @Test
    public void testGetUsersRoute() throws Exception {
        givenRequestContainsParameters(ImmutableMap.of(
                RouteServlet.PARAM_USER, new String[]{"bob"}
        ));

        routeServlet.doGet(request,response);

        verify(request).getRequestDispatcher(RouteServlet.ROUTE_USER + "bob");
    }
//
//    @Test
//    public void testGetWithCurrentUserAction() throws Exception {
//        User user = givenUserExists(UserBuilder.anUser().
//                withNames("username", "nickname").withPassword("pass"));
//        givenRequestContainsParameters(ImmutableMap.of(
//                UsersServlet.PARAM_ACTION, new String[]{UsersServlet.Action.CURRENT.toString()}
//        ));
//        givenRequestContainsUserWithRoles(user.getUsername(),"role1","role2");
//
//        usersServlet.doGet(request,response);
//
//        assertThat(stringWriter.toString(), sameJSONAs(fileContentAsString("current-user.json")));
//    }
//
//    private User givenUserExists(UserBuilder userBuilder) {
//        User user = userBuilder.build();
//        when(userRepo.findByField(eq(User.DB_FIELD_USERNAME), eq(user.getUsername()))).
//                thenReturn(Arrays.asList(user));
//        return user;
//    }
//
//    @Test
//    public void testGetWithUserInfoAction() throws Exception {
//        User user = givenUserExists(UserBuilder.anUser().
//                withNames("username","nickname").withPassword("pass").withPhotoUrl("/some/photo.jpeg"));
//        givenRequestContainsParameters(ImmutableMap.of(
//                UsersServlet.PARAM_ACTION, new String[]{UsersServlet.Action.USER_INFO.toString()},
//                UsersServlet.PARAM_USER, new String[]{user.getUsername()}
//        ));
//
//        usersServlet.doGet(request,response);
//
//        assertThat(stringWriter.toString(), sameJSONAs(fileContentAsString("user-with-photo.json")));
//    }
//
//    @Test
//    public void testPostNotAllowed() throws Exception {
//        usersServlet.doPost(request,response);
//        assertEquals(responseStatusCode, ServletUtils.METHOD_NOT_ALLOWED);
//
//    }
}