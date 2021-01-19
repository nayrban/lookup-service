package com.katas.lookupservice.service;

import com.katas.lookupservice.domain.Lookup;
import com.katas.lookupservice.dto.ThinqCredentialsLookup;
import com.katas.lookupservice.dto.ThinqCredentialsResponse;
import com.katas.lookupservice.utils.LookupUtils;
import com.katas.lookupservice.utils.ThinQActions;
import com.katas.lookupservice.utils.ThinQConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ThingQConfigurationService {

    private static final Long ORDINAL = 1L;

    private final LookupService lookupService;
    private final ThingQAPIService thingQAPIService;

    @Autowired
    public ThingQConfigurationService(LookupService lookupService, ThingQAPIService thingQAPIService) {
        this.lookupService = lookupService;
        this.thingQAPIService = thingQAPIService;
    }

    public ThinqCredentialsResponse saveOrUpdate(ThinqCredentialsLookup credentials) {
        log.info("Call to: ThingQConfigurationService.saveOrUpdate()");
        List<Lookup> configurationValues = getConfigurationValues();
        if (configurationValues.isEmpty()) {
            return save(credentials);
        }

        return update(credentials, configurationValues);
    }

    public ThinqCredentialsResponse getCredentials() {
        log.info("Call to: ThingQConfigurationService.getCredentials()");
        return fromLookup(getConfigurationValues());
    }

    private ThinqCredentialsResponse save(ThinqCredentialsLookup credentials) {
        Long createdBy = credentials.getCreatedBy();
        Lookup usernameLookup = toLookup(ThinQConstants.USERNAME, credentials.getUsername(), createdBy);
        Lookup accountLookup = toLookup(ThinQConstants.ACCOUNT_ID, credentials.getAccountId(), createdBy);
        Lookup tokenLookup = toLookup(ThinQConstants.TOKEN, credentials.getToken(), createdBy);
        Lookup addressLookup = toLookup(ThinQConstants.IP_ADDRESS, credentials.getIpAddress(), createdBy);
        return fromLookup(this.lookupService.saveAll(Arrays.asList(usernameLookup, accountLookup, tokenLookup, addressLookup)));
    }

    private ThinqCredentialsResponse update(ThinqCredentialsLookup credentials, List<Lookup> configurationValues) {
        configurationValues.forEach(lookup -> updateConfiguration(lookup, credentials));
        return fromLookup(lookupService.saveAll(configurationValues));
    }

    private void updateConfiguration(Lookup lookup, ThinqCredentialsLookup credentials) {
        switch (lookup.getName()) {
            case ThinQConstants.USERNAME:
                lookup.setValue(credentials.getUsername());
                break;
            case ThinQConstants.ACCOUNT_ID:
                lookup.setValue(credentials.getAccountId());
                break;
            case ThinQConstants.TOKEN:
                lookup.setValue(credentials.getToken());
                break;
            case ThinQConstants.IP_ADDRESS:
                updateIpAddressWhenRequired(lookup, credentials);
                break;
            default:
                throw new RuntimeException("Configuration value is not expected");
        }
        lookup.setModifiedBy(credentials.getCreatedBy());
    }

    private void updateIpAddressWhenRequired(Lookup lookup, ThinqCredentialsLookup credentials) {
        if (!credentials.getIpAddress().equals("-")) {
            String oldIP = lookup.getValue();
            lookup.setValue(credentials.getIpAddress());

            String url = getConfigurationValue(ThinQActions.UPDATE_IP_ADDRESS)
                    .map(Lookup::getValue)
                    .orElseThrow(() -> new RuntimeException("ThinQ URL Not found"));

            //TODO I suppose they do something with the response
            String result = thingQAPIService.doSomething(url, oldIP, getCredentials());
            Assert.hasLength(result, "Response is not empty");
        }
    }

    private Optional<Lookup> getConfigurationValue(String name) {
        return lookupService.findByNameAndCategory(name, ThinQConstants.CONFIGURATION);
    }

    private List<Lookup> getConfigurationValues() {
        return lookupService.findByNamesAndCategory(Arrays.asList(ThinQConstants.USERNAME, ThinQConstants.ACCOUNT_ID,
                ThinQConstants.TOKEN, ThinQConstants.IP_ADDRESS), ThinQConstants.CONFIGURATION);
    }

    private ThinqCredentialsResponse fromLookup(List<Lookup> emailConfiguration) {
        Lookup usernameLookup = LookupUtils.findConfigurationOrThrow(emailConfiguration, ThinQConstants.USERNAME);
        Lookup accountIdLookup = LookupUtils.findConfigurationOrThrow(emailConfiguration, ThinQConstants.ACCOUNT_ID);
        Lookup tokenLookup = LookupUtils.findConfigurationOrThrow(emailConfiguration, ThinQConstants.TOKEN);
        Lookup ipAddressLookup = LookupUtils.findConfigurationOrThrow(emailConfiguration, ThinQConstants.IP_ADDRESS);

        return new ThinqCredentialsResponse(usernameLookup.getValue(), accountIdLookup.getValue(), tokenLookup.getValue(), ipAddressLookup.getValue());
    }

    private Lookup toLookup(String name, String value, Long modifiedBy) {
        return LookupUtils.toLookup(name, ThinQConstants.CONFIGURATION, value, ORDINAL, modifiedBy);
    }
}