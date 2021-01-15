package com.katas.lookupservice.service;

import com.katas.lookupservice.dto.CredentialsLookup;
import com.katas.lookupservice.dto.EmailCredentialsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EmailConfigurationServiceTest {

    @Autowired
    private EmailConfigurationService emailConfigurationService;


    @Test
    public void exceptionWhenCredentialNotFound() {
        assertThrows(RuntimeException.class, () -> emailConfigurationService.getCredentials());
    }

    @Test
    public void whenSaved_thenCredentialsShouldBeFound() {
        CredentialsLookup something = new CredentialsLookup();
        something.setEmail("something@gmail.com");
        something.setPassword("password1234");
        something.setCreatedBy(12L);

        EmailCredentialsResponse credentialsResponse = emailConfigurationService.saveOrUpdate(something);
        EmailCredentialsResponse found = emailConfigurationService.getCredentials();

        assertEquals(credentialsResponse, found);
    }

    @Test
    public void whenUpdated_thenCredentialsNotMatch() {
        CredentialsLookup something = new CredentialsLookup();
        something.setEmail("something@gmail.com");
        something.setPassword("password1234");
        something.setCreatedBy(12L);

        EmailCredentialsResponse credentialsResponse = emailConfigurationService.saveOrUpdate(something);

        something.setEmail("somethingelse@gmail.com");
        something.setPassword("password12345");
        something.setCreatedBy(13L);
        EmailCredentialsResponse found = emailConfigurationService.saveOrUpdate(something);

        assertNotEquals(credentialsResponse, found);
    }
}
