package com.example.zstd.microblog.dto;


import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class RegistrationDataTest {


    @Test
    public void testValidate() throws Exception {
        RegistrationData data = new RegistrationData("user","password","fdsf","fdsf","fdsf","fdsf");
        assertFalse(data.validate().isEmpty());

    }
}