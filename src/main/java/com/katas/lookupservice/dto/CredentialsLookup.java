package com.katas.lookupservice.dto;

import lombok.Data;

@Data
public class CredentialsLookup {
    private String email;
    private String password;
    private Long createdBy;
}
