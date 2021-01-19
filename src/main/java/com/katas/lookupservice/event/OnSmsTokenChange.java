package com.katas.lookupservice.event;

import com.katas.lookupservice.service.LookupService;
import com.katas.lookupservice.utils.ThinQConstants;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class OnSmsTokenChange {

    private final LookupService lookupService;

    public OnSmsTokenChange(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @Async
    @EventListener
    public void handleSmsTokenChangeEvent(TokenChangeEvent event) {
        lookupService.findByNameAndCategory(ThinQConstants.TOKEN, ThinQConstants.CONFIGURATION)
                .ifPresentOrElse(lookup -> updateToken(event, lookup), throwWhenEmpty());
    }

    private void updateToken(TokenChangeEvent event, com.katas.lookupservice.domain.Lookup lookup) {
        lookup.setValue(event.getToken());
        lookupService.save(lookup);
    }

    private Runnable throwWhenEmpty() {
        return () -> {
            throw new RuntimeException("Token Configuration not Found");
        };
    }
}