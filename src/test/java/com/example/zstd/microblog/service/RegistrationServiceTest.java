package com.example.zstd.microblog.service;

import com.example.zstd.microblog.dto.RegistrationData;
import com.example.zstd.microblog.dto.RegistrationDataBuilder;
import com.example.zstd.microblog.model.User;
import com.example.zstd.microblog.repository.UserRepo;
import com.example.zstd.microblog.repository.UserRoleRepo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.example.zstd.microblog.dto.RegistrationDataBuilder.aRegistrationData;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RegistrationServiceTest {

    //test data
    private static final String USERNAME = "any-user-name";
    private static final RegistrationData REGISTRATION_DATA =
            new RegistrationData("user","password","fdsf","fdsf","fdsf","fdsf");

    private RegistrationService registrationService;
    private UserRepo userRepo;
    private UserRoleRepo userRoleRepo;
    private User createdUser;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        registrationService = new RegistrationService();
        userRepo = mock(UserRepo.class);
        userRoleRepo = mock(UserRoleRepo.class);
        registrationService.setUserRepo(userRepo);
        registrationService.setUserRoleRepo(userRoleRepo);
    }

    @Test
    public void testCreate() throws Exception {
        RegistrationData data = givenRegistrationData(aRegistrationData().withUsername(USERNAME));

        whenCreateNewUser(data);

        thenNoException();
        thenRepositoriesCalled();
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