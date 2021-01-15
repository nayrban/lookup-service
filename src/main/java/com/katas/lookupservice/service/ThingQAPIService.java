package com.katas.lookupservice.service;

import com.katas.lookupservice.dto.ThinqCredentialsResponse;
import com.katas.lookupservice.utils.ThinQActions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ThingQAPIService {

    private final RestTemplate restTemplate;

    @Autowired
    public ThingQAPIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    //TODO I am not sure what was the purpose of this method in the Origanl
    public String doSomething(String url, String oldIP, ThinqCredentialsResponse credentials) {
        log.info("Call to: LookupService.createThinqLookupCredentialsConfiguration()");

        url = url.replace(ThinQActions.ACCOUNT_ID_PARAM, credentials.getAccountId()).replace(ThinQActions.OLD_IP_PARAM, oldIP)
                .replace(ThinQActions.NEW_IP_PARAM, credentials.getIpAddress());

        String jsonBody = "";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(credentials.getUsername(), credentials.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        return this.restTemplate.exchange(url, HttpMethod.PUT, entity, String.class).getBody();
    }
}
