package com.example.zstd.microblog.service;

import com.example.zstd.microblog.dto.RegistrationData;
import com.example.zstd.microblog.repository.UserRepo;
import com.example.zstd.microblog.repository.UserRoleRepo;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class RegistrationServiceTest {

    //test data
    private static final RegistrationData REGISTRATION_DATA =
            new RegistrationData("user","password","fdsf","fdsf","fdsf","fdsf");

    private RegistrationService registrationService;
    private UserRepo userRepo;
    private UserRoleRepo userRoleRepo;

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
        registrationService.create(REGISTRATION_DATA);

    }
}