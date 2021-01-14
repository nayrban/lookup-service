package com.katas.lookupservice.service;


import com.katas.lookupservice.domain.Lookup;
import com.katas.lookupservice.dto.CredentialsLookup;
import com.katas.lookupservice.dto.EmailCredentialsResponse;
import com.katas.lookupservice.dto.ThinqCredentialsLookup;
import com.katas.lookupservice.dto.ThinqCredentialsResponse;
import com.katas.lookupservice.exception.RequestException;
import com.katas.lookupservice.repository.LookupRepository;
import com.katas.lookupservice.utils.ThinQActions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LookupService {

    // <editor-fold  desc="Attributes">
    private LookupRepository lookupRepository;
    private RestTemplate restTemplate;

    // </editor-fold>

    // <editor-fold  desc="Constructor">
    @Autowired
    public LookupService(LookupRepository lookupRepository, RestTemplate restTemplate) {
        this.lookupRepository = lookupRepository;
        this.restTemplate = restTemplate;
    }

    // </editor-fold>

    // <editor-fold  desc="Methods">

    public List<Lookup> createLookupCredentialsConfiguration(CredentialsLookup credentialsLookup) {
        log.info("Call to: LookupService.createLookupCredentialsConfiguration()");
        List<Lookup> list = new ArrayList<>();

        try {
            Lookup emailLookup = getLookupByName("EMAIL", "EMAIL_CONFIGURATION");
            emailLookup.setValue(credentialsLookup.getEmail());
            emailLookup.setModifiedBy(credentialsLookup.getCreatedBy());

            this.lookupRepository.save(emailLookup);

            Lookup passwordLookup = getLookupByName("PASSWORD", "EMAIL_CONFIGURATION");
            passwordLookup.setValue(credentialsLookup.getPassword());
            passwordLookup.setModifiedBy(credentialsLookup.getCreatedBy());

            this.lookupRepository.save(passwordLookup);

            list.add(emailLookup);
            list.add(passwordLookup);
        } catch (Exception e) {
            log.warn("Exception in: LookupService.createLookupCredentialsConfiguration()");
            throw new RequestException("bob.error.LookupService.createLookupCredentialsConfiguration", e.getMessage());
        }

        return list;
    }

    public List<Lookup> createThinqLookupCredentialsConfiguration(ThinqCredentialsLookup createThinqCredentialsLookup) {
        log.info("Call to: LookupService.createThinqLookupCredentialsConfiguration()");
        List<Lookup> list = new ArrayList<>();

        try {
            Lookup usernameLookup = getLookupByName("USERNAME", "THINQ_CONFIGURATION");
            usernameLookup.setValue(createThinqCredentialsLookup.getUsername());
            usernameLookup.setModifiedBy(createThinqCredentialsLookup.getCreatedBy());

            this.lookupRepository.save(usernameLookup);

            Lookup accountIdLookup = getLookupByName("ACCOUNT ID", "THINQ_CONFIGURATION");
            accountIdLookup.setValue(createThinqCredentialsLookup.getAccountId());
            accountIdLookup.setModifiedBy(createThinqCredentialsLookup.getCreatedBy());

            this.lookupRepository.save(accountIdLookup);

            Lookup tokenLookup = getLookupByName("TOKEN", "THINQ_CONFIGURATION");
            tokenLookup.setValue(createThinqCredentialsLookup.getToken());
            tokenLookup.setModifiedBy(createThinqCredentialsLookup.getCreatedBy());

            this.lookupRepository.save(tokenLookup);

            Lookup ipAddressLookup = getLookupByName("IP_ADDRESS", "THINQ_CONFIGURATION");
            String oldIP = ipAddressLookup.getValue();
            if (createThinqCredentialsLookup.getIpAddress() != "-") {
                ipAddressLookup.setValue(createThinqCredentialsLookup.getIpAddress());
                ipAddressLookup.setModifiedBy(createThinqCredentialsLookup.getCreatedBy());

                try {
                    ThinqCredentialsResponse credentials = getThinqCredentials();
                    String url = getThinqURLByAction(ThinQActions.UPDATE_IP_ADDRESS).replace(ThinQActions.ACCOUNT_ID_PARAM, credentials.getAccountId());
                    url = url.replace(ThinQActions.OLD_IP_PARAM, oldIP);
                    url = url.replace(ThinQActions.NEW_IP_PARAM, createThinqCredentialsLookup.getIpAddress());

                    String jsonBody = "";

                    HttpHeaders headers = new HttpHeaders();
                    headers.setBasicAuth(credentials.getUsername(), credentials.getToken());
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

                    String body = this.restTemplate.exchange(url, HttpMethod.PUT, entity, String.class).getBody();

                    this.lookupRepository.save(ipAddressLookup);
                } catch (Exception e) {
                    log.warn("Exception in: LookupService.createThinqLookupCredentialsConfiguration()");
                    throw new RequestException("bob.error.LookupService.createThinqLookupCredentialsConfiguration", "Error in createThinqLookupCredentialsConfiguration() -> " + e.getCause());
                }
            }

            list.add(usernameLookup);
            list.add(accountIdLookup);
            list.add(tokenLookup);
            list.add(ipAddressLookup);
        } catch (Exception e) {
            log.warn("Exception in: ACWController.createThinqLookupCredentialsConfiguration()");
            throw new RequestException("bob.error.LookupService.createThinqLookupCredentialsConfiguration", e.getMessage());
        }

        return list;
    }

    public List<Lookup> getLookupListFromCategory(String category) {
        log.info("Call to: LookupService.getLookupListFromCategory()");
        List<Lookup> list = null;
        try {
            list = this.lookupRepository.findByCategoryAndDeletedFalseOrderByOrdinalAsc(category);
        } catch (Exception e) {
            log.warn("Exception in: LookupService.getLookupListFromCategory()");
            throw new RequestException("bob.error.LookupService.getLookupListFromCategory", e.getMessage());
        }
        return list;
    }

    public Optional<Lookup> getLookupById(Long id) {
        log.info("Call to: LookupService.getLookupById()");
        Optional<Lookup> result;
        try {
            result = this.lookupRepository.findById(id);
        } catch (Exception e) {
            log.warn("Exception in: LookupService.getLookupById()");
            throw new RequestException("bob.error.LookupService.getLookupById", e.getMessage());
        }

        return result;
    }

    public Lookup getLookupByName(String name, String category) {
        log.info("Call to: LookupService.getLookupByName()");
        //TODO we  have to update this inn the future
        return this.lookupRepository.findByNameAndCategory(name, category).orElse(null);
    }

    public Lookup getLookupByValue(Long value, String category) {
        return this.lookupRepository.findByOrdinalAndCategory(value, category)
                .orElseThrow(() -> new RuntimeException(String.format("Lookup with ordinal %1d and category %2s not found", value, category)));
    }

    public EmailCredentialsResponse getEmailCredentials() {
        log.info("Call to: LookupService.getEmailCredentials()");
        EmailCredentialsResponse result = null;
        try {
            Lookup emailLookup = getLookupByName("EMAIL", "EMAIL_CONFIGURATION");
            Lookup passwordLookup = getLookupByName("PASSWORD", "EMAIL_CONFIGURATION");

            result = new EmailCredentialsResponse(emailLookup.getValue(), passwordLookup.getValue());

        } catch (Exception e) {
            log.warn("Exception in: LookupService.getEmailCredentials()");
            throw new RequestException("bob.error.LookupService.getEmailCredentials", e.getMessage());
        }

        return result;
    }

    public ThinqCredentialsResponse getThinqCredentials() {
        log.info("Call to: LookupService.getThinqCredentials()");
        ThinqCredentialsResponse result = null;
        try {
            Lookup usernameLookup = getLookupByName("USERNAME", "THINQ_CONFIGURATION");
            Lookup accounIdLookup = getLookupByName("ACCOUNT ID", "THINQ_CONFIGURATION");
            Lookup tokenLookup = getLookupByName("TOKEN", "THINQ_CONFIGURATION");
            Lookup ipAddressLookup = getLookupByName("IP_ADDRESS", "THINQ_CONFIGURATION");

            result = new ThinqCredentialsResponse(usernameLookup.getValue(), accounIdLookup.getValue(), tokenLookup.getValue(), ipAddressLookup.getValue());

        } catch (Exception e) {
            log.warn("Exception in: LookupService.getThinqCredentials()");

            throw new RequestException("bob.error.LookupService.getThinqCredentials", e.getMessage());
        }

        return result;
    }

    public String getThinqURLByAction(String action) {
        log.info("Call to: LookupService.getThinqURLByAction()");
        String result = null;
        try {

            result = getLookupByName(action, "THINQ_CONFIGURATION").getValue();

        } catch (Exception e) {
            log.warn("Exception in: LookupService.getThinqURLByAction()");

            throw new RequestException("bob.error.LookupService.getThinqURLByAction", e.getMessage());
        }

        return result;
    }

    public Boolean saveLookup(Lookup lookup) {
        Boolean result = false;
        try {
            this.lookupRepository.save(lookup);
            result = true;
        } catch (Exception e) {
            throw new RequestException("bob.error.LookupService.saveLookup", e.getMessage());
        }

        return result;
    }
    // </editor-fold>
}
