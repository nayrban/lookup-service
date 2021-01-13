package com.katas.lookupservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ThinqCredentialsResponse {
    private String username;
    private  String accountId;
    private String token;
    private String ipAddress;
}
