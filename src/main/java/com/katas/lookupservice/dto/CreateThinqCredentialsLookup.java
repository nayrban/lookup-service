package com.katas.lookupservice.dto;

import lombok.Data;

@Data
public class CreateThinqCredentialsLookup {
    private String accountId;
    private String username;
    private String token;
    private Long createdBy;
    private String ipAddress;
}
