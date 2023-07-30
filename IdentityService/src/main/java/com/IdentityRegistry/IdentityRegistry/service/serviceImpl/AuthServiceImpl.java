package com.IdentityRegistry.IdentityRegistry.service.serviceImpl;


import com.IdentityRegistry.IdentityRegistry.dto.*;
import com.IdentityRegistry.IdentityRegistry.entity.Roles;
import com.IdentityRegistry.IdentityRegistry.entity.User;
import com.IdentityRegistry.IdentityRegistry.entity.UserStatus;
import com.IdentityRegistry.IdentityRegistry.repository.RoleRepository;
import com.IdentityRegistry.IdentityRegistry.repository.UserRepository;
import com.IdentityRegistry.IdentityRegistry.security.JwtTokenProvider;
import com.IdentityRegistry.IdentityRegistry.security.token.Token;
import com.IdentityRegistry.IdentityRegistry.security.token.TokenRepository;
import com.IdentityRegistry.IdentityRegistry.security.token.TokenType;
import com.IdentityRegistry.IdentityRegistry.service.serviceInterface.AuthService;
import com.IdentityRegistry.IdentityRegistry.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private RoleRepository roleRepository;
    private TokenRepository tokenRepository;


    @Override
    public AuthResponse login(LoginDto loginDto) {
        User foundUser = userRepository.findByEmail(loginDto.getEmail());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(foundUser.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtTokenProvider.generateToken(authentication));
        revokeAllUserTokens(foundUser);
        saveUserToken(authentication, foundUser);
        return authResponse;
    }


    @Override
    public ResponseEntity<?> signup(UserRequest userRequest){

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return new ResponseEntity<>("Email is already taken", HttpStatus.BAD_REQUEST);
        }

        String phoneNumberRegex = "(^$|\\d{13})";//phone number accepts only 13 digits starting with 234
        if (!isInputValid(userRequest.getPhoneNumber(), phoneNumberRegex)) {
            return new ResponseEntity<>("Invalid Phone number", HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(ResponseUtils.generateAccountNumber(ResponseUtils.LENGTH_OF_ACCOUNT_NUMBER))
                .accountBalance(userRequest.getAccountBalance())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status(UserStatus.ACTIVE)
                .dateOfBirth(userRequest.getDateOfBirth())
                .build();

        Roles role = roleRepository.findByRoleName("ROLE_USER").orElseThrow();
        user.setRoles(Collections.singleton(role));
        User savedUser = userRepository.save(user);
        String accountDetails = savedUser.getFirstName() + " " + savedUser.getLastName() + " "+ savedUser.getOtherName() + "\nAccount Number: " + savedUser.getAccountNumber();
        return new ResponseEntity<>(UserResponse.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.USER_REGISTERED_SUCCESS)
                .userData(UserData.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                        .build()).
                build(),HttpStatus.CREATED);
    }


    private void saveUserToken(Authentication authentication, User user) {
        var token = Token.builder()
                .user(user)
                .token(jwtTokenProvider.generateToken(authentication))
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensById(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    //boolean for checking phone number
    private boolean isInputValid(String input, String regex) {
        return Pattern.compile(regex)
                .matcher(input)
                .matches();
    }
}
