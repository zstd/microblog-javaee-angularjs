package com.example.zstd.microblog.servlet.rest;

import com.example.zstd.microblog.model.FollowData;
import com.example.zstd.microblog.service.FollowDataService;
import com.example.zstd.microblog.service.ServiceLocator;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

public class FollowDataServletTest extends ServletTestBase {

    private static final Logger LOG = Logger.getLogger(FollowDataServletTest.class.getName());

    private FollowDataService followDataService;

    private FollowDataServlet followDataServlet;

    private static final String USER = "bob";

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

        assertEquals(FollowDataServlet.EMPTY_RESULT, stringWriter.toString());
    }


    @Test
    public void testGetFollowingData() throws Exception {
        givenRequestContainsParameters(ImmutableMap.of(
                FollowDataServlet.PARAM_FOLLOWING, new String[]{USER}
        ));
        givenThereAreFollowingDataFor(USER,followData(USER,0),followData(USER,1));

        followDataServlet.doGet(request, response);
        LOG.info(stringWriter.toString());
        assertThat(stringWriter.toString(), sameJSONAs(fileContentAsString("following-data-list.json")).allowingAnyArrayOrdering());
    }

    @Test
    public void testPostFollowingData() throws Exception {
        givenRequestContainsPayload(new ByteArrayInputStream(fileContentAsString("following-data-create-request.json").getBytes()));
        givenFollowDataCanBeCreated("follower #0 of bob", "following #0 of bob");

        followDataServlet.doPost(request, response);

        assertThat(stringWriter.toString(), sameJSONAs(fileContentAsString("following-data.json")).allowingAnyArrayOrdering());
    }

    @Test
    public void testDeleteFollowingData() throws Exception {
        String following = "following-" + USER,
                follower = "follower-" + USER;
        givenRequestContainsParameters(ImmutableMap.of(
                FollowDataServlet.PARAM_FOLLOWING, new String[]{following},
                FollowDataServlet.PARAM_FOLLOWER, new String[]{follower}
        ));

        followDataServlet.doDelete(request, response);

        verify(followDataService).deleteFollowerData(eq(follower), eq(following));
    }

    private void givenFollowDataCanBeCreated(String follower, String following) {
        when(followDataService.addFollowerData(eq(follower),eq(following))).
                thenReturn(new FollowData(0L, follower, following));
    }

    private FollowData followData(String user, int index) {
        return new FollowData(Long.valueOf(index),"follower #" + index + " of " +user,
                "following #" + index + " of " + user);
    }

    private void givenThereAreFollowingDataFor(String user, FollowData...followData) {
        when(followDataService.getFollowingData(eq(user))).thenReturn(Arrays.asList(followData));
    }


}