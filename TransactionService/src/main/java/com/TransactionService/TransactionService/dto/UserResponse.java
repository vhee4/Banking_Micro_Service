package com.TransactionService.TransactionService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
//    public Object getUserData;
    private String responseCode;
    private String responseMessage;
    private UserData userData;

}
