package com.IdentityRegistry.IdentityRegistry.service.serviceInterface;


import com.IdentityRegistry.IdentityRegistry.dto.AuthResponse;
import com.IdentityRegistry.IdentityRegistry.dto.LoginDto;
import com.IdentityRegistry.IdentityRegistry.dto.UserRequest;
//import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    AuthResponse login(LoginDto loginDto);

    ResponseEntity<?> signup(UserRequest userRequest);

//    ResponseEntity<?> verifyOtp(OtpDto otpDto) throws UnirestException;
}
