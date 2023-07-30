package com.IdentityRegistry.IdentityRegistry.controller;

import com.IdentityRegistry.IdentityRegistry.dto.AuthResponse;
import com.IdentityRegistry.IdentityRegistry.dto.LoginDto;
import com.IdentityRegistry.IdentityRegistry.dto.UserRequest;
import com.IdentityRegistry.IdentityRegistry.repository.RoleRepository;
import com.IdentityRegistry.IdentityRegistry.service.serviceInterface.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private AuthenticationManager authenticationManager;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserRequest userRequest){
        return ResponseEntity.ok(authService.signup(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @PostMapping("logout")
    public String logout() {
        return "logout successful";
    }


}
