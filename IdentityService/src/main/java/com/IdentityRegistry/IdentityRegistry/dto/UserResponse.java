package com.IdentityRegistry.IdentityRegistry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String responseCode;
    private String responseMessage;
    private UserData userData;

}
