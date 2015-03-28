package com.example.zstd.microblog.service;

import com.example.zstd.microblog.dto.RegistrationData;
import com.example.zstd.microblog.dto.RegistrationDataBuilder;
import com.example.zstd.microblog.exception.RegistrationException;
import com.example.zstd.microblog.exception.RepositoryException;
import com.example.zstd.microblog.model.User;
import com.example.zstd.microblog.repository.UserRepo;
import com.example.zstd.microblog.repository.UserRoleRepo;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;

import static com.example.zstd.microblog.dto.RegistrationDataBuilder.aRegistrationData;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.endsWith;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest {

    //test data
    private static final String USERNAME = "any-user-name";
    private static final String NICKNAME = "any-nickname-name";
    private static final RegistrationData REGISTRATION_DATA =
            new RegistrationData("user","password","fdsf","fdsf","fdsf","fdsf");

    private RegistrationService registrationService;
    private UserRepo userRepo;
    private UserRoleRepo userRoleRepo;
    private User createdUser;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUpClass() throws Exception {
        userRepo = mock(UserRepo.class);
        userRoleRepo = mock(UserRoleRepo.class);
        ServiceLocator.initialize(ImmutableMap.<Class, Object>of(
                UserRepo.class, userRepo,
                UserRoleRepo.class, userRoleRepo
        ));
        registrationService = new RegistrationService();
    }


    @Test
    public void testCreate() throws Exception {
        RegistrationData data = givenRegistrationData(aRegistrationData().withUsername(USERNAME));

        whenCreateNewUser(data);

        thenNoException();
        thenRepositoriesCalled();
    }

    @Test(expected = RegistrationException.class)
    public void testCreateFailOnUserSave() throws Exception {
        RegistrationData data = givenRegistrationData(aRegistrationData().withUsername(USERNAME).withNickname(NICKNAME));
        givenUserRepoError();

        whenCreateNewUser(data);

        expectedException.expect(instanceOf(RegistrationException.class));
        expectedException.expectMessage(endsWith("already exists"));
    }

    @Test(expected = RegistrationException.class)
    public void testCreateFailOnDuplicateUsername() throws Exception {
        RegistrationData data = givenRegistrationData(aRegistrationData().withUsername(USERNAME).withNickname(NICKNAME));
        givenUserWithFieldExists(User.DB_FIELD_USERNAME,USERNAME);

        whenCreateNewUser(data);
        // TODO research this
        expectedException.expect(instanceOf(RegistrationException.class));
        expectedException.expectMessage(endsWith("already exi2222sts"));
    }

    private void givenUserRepoError() {
        doThrow(new RepositoryException("expected-failure-to-save-user")).when(userRepo).save(any(User.class));
    }


    private void givenUserWithFieldExists(String dbFieldName, String doFieldValue) {
        when(userRepo.findByField(dbFieldName, doFieldValue)).thenReturn(Collections.singletonList(new User()));
    }

    private void thenRepositoriesCalled() {
        verify(userRepo).save(any(User.class));
        verify(userRoleRepo).add(eq(USERNAME),eq(RegistrationService.MAIN_ROLE));
    }

    private void thenNoException() {
        //assertEquals(expectedException,ExpectedException.none());
    }

    private void whenCreateNewUser(RegistrationData data) {
        createdUser = registrationService.create(data);
    }

    private RegistrationData givenRegistrationData(RegistrationDataBuilder builder) {
        return builder.build();
    }
}