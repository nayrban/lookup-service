package com.katas.lookupservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailCredentialsResponse {
    private String email;
    private String password;
}
