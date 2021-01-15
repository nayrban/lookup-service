package com.katas.lookupservice.service;

import com.katas.lookupservice.domain.Lookup;
import com.katas.lookupservice.dto.CredentialsLookup;
import com.katas.lookupservice.dto.EmailCredentialsResponse;
import com.katas.lookupservice.utils.LookupUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class EmailConfigurationService {

    private static final Long ORDINAL = 1L;
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";
    private static final String CONFIGURATION = "EMAIL_CONFIGURATION";

    private final LookupService lookupService;

    @Autowired
    public EmailConfigurationService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public EmailCredentialsResponse saveOrUpdate(CredentialsLookup credentialsLookup) {
        log.info("Call to: EmailConfigurationService.saveOrUpdate()");
        List<Lookup> configurationValues = getConfigurationValues();
        if (configurationValues.isEmpty())
            return save(credentialsLookup);

        return update(credentialsLookup, configurationValues);
    }

    public EmailCredentialsResponse getCredentials() {
        log.info("Call to: EmailConfigurationService.getCredentials()");
        return fromLookup(getConfigurationValues());
    }

    private EmailCredentialsResponse save(CredentialsLookup credentialsLookup) {
        Long createdBy = credentialsLookup.getCreatedBy();
        Lookup emailLookup = toLookup(EMAIL, credentialsLookup.getEmail(), createdBy);
        Lookup passwordLookup = toLookup(PASSWORD, credentialsLookup.getPassword(), createdBy);
        return fromLookup(this.lookupService.saveAll(Arrays.asList(emailLookup, passwordLookup)));
    }

    private EmailCredentialsResponse update(CredentialsLookup credentialsLookup, List<Lookup> configurationValues) {
        log.info("Call to: EmailConfigurationService.update()");
        configurationValues.forEach(lookup -> updateConfiguration(credentialsLookup, lookup));
        return fromLookup(this.lookupService.saveAll(configurationValues));
    }

    private List<Lookup> getConfigurationValues() {
        return lookupService.findByNamesAndCategory(Arrays.asList(EMAIL, PASSWORD), CONFIGURATION);
    }

    private void updateConfiguration(CredentialsLookup credentialsLookup, Lookup lookup) {
        if (lookup.getName().equals(EMAIL)) {
            lookup.setValue(credentialsLookup.getEmail());
        } else if (lookup.getName().equals(PASSWORD)) {
            lookup.setValue(credentialsLookup.getPassword());
        }

        lookup.setModifiedBy(credentialsLookup.getCreatedBy());
    }

    private EmailCredentialsResponse fromLookup(List<Lookup> emailConfiguration) {
        Lookup emailLookup = LookupUtils.findConfigurationOrThrow(emailConfiguration, EMAIL);
        Lookup passwordLookup = LookupUtils.findConfigurationOrThrow(emailConfiguration, PASSWORD);

        return new EmailCredentialsResponse(emailLookup.getValue(), passwordLookup.getValue());
    }

    private Lookup toLookup(String name, String value, Long modifiedBy) {
        return LookupUtils.toLookup(name, CONFIGURATION, value, ORDINAL, modifiedBy);
    }
}