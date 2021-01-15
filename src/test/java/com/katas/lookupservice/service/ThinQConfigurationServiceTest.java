package com.katas.lookupservice.service;

import com.katas.lookupservice.dto.EmailCredentialsResponse;
import com.katas.lookupservice.dto.ThinqCredentialsLookup;
import com.katas.lookupservice.dto.ThinqCredentialsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//TODO this test case is not exhaustive
@SpringBootTest
public class ThinQConfigurationServiceTest {

    @Autowired
    private ThingQConfigurationService thingQConfigurationService;

    @Test
    public void exceptionWhenCredentialNotFound() {
        assertThrows(RuntimeException.class, () -> thingQConfigurationService.getCredentials());
    }

    @Test
    public void whenSaved_thenCredentialsShouldBeFound() {
        ThinqCredentialsLookup something = new ThinqCredentialsLookup();
        something.setAccountId("something@gmail.com");
        something.setToken("password1234");
        something.setUsername("password1234");
        something.setIpAddress("192.168.0.1");
        something.setCreatedBy(12L);

        ThinqCredentialsResponse credentialsResponse = thingQConfigurationService.saveOrUpdate(something);
        ThinqCredentialsResponse found = thingQConfigurationService.getCredentials();

        assertEquals(credentialsResponse, found);
    }
}
