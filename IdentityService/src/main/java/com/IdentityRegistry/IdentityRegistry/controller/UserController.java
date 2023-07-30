package com.IdentityRegistry.IdentityRegistry.controller;

import com.IdentityRegistry.IdentityRegistry.dto.TransactionRequest;
import com.IdentityRegistry.IdentityRegistry.dto.UserData;
import com.IdentityRegistry.IdentityRegistry.dto.UserResponse;
import com.IdentityRegistry.IdentityRegistry.service.serviceInterface.UserService;
//import io.swagger.v3.oas.annotations.ExternalDocumentation;
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.info.Contact;
//import io.swagger.v3.oas.annotations.info.Info;
//import io.swagger.v3.oas.annotations.info.License;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
//@OpenAPIDefinition(
//        info = @Info(
//                title = "Spring Boot Banking Application",
//                description = "Spring Boot Banking Application REST APIs Documentation",
//                version = "v1.0",
//                contact = @Contact(
//                        name = "Chidinma",
//                        email = "vivianafogu@gmail.com",
//                        url = "https://github.com/vhee4"
//                ),
//                license = @License(
//                        name = "Apache 2.0",
//                        url = "https://github.com/vhee4"
//                )
//
//        ),
//        externalDocs = @ExternalDocumentation(
//                description = "Spring Boot Banking Application",
//                url = "https://github.com/vhee4"
//        )
////        @Tag(
////
////        )
//
//)
public class UserController {

    private final UserService userService;

    @GetMapping("/{userid}")
    public UserResponse fetchUser(@PathVariable("userid") Long userId){
        return userService.fetchUser(userId);
    }

    @GetMapping("allUsers")
    public List<UserResponse> allUsers(){
        return userService.allUsers();
    }

    @GetMapping("/balEnquiry")
    public UserResponse balanceEnquiry(@RequestParam("accountNumber") String accountNumber){
        return userService.balanceEnquiry(accountNumber);
    }
    @GetMapping("/nameEnquiry")
    public UserResponse nameEnquiry(@RequestParam("accountNumber") String accountNumber){
        return userService.nameEnquiry(accountNumber);
    }

    @DeleteMapping("/delete")
    public UserResponse deleteUser(@RequestParam("accountNumber") String accountNumber){
        return userService.deleteUser(accountNumber);
    }

    @PostMapping("credit-and-update-accountBalance")
    public ResponseEntity<UserResponse> creditAndUpdateUserBalance(@RequestBody TransactionRequest transactionRequest){
        return ResponseEntity.ok(userService.UpdateUserBalanceAfterCredit(transactionRequest));
    }

    @PostMapping("debit-and-update-accountBalance")
    public ResponseEntity<UserResponse> debitAndUpdateUserBalance(@RequestBody TransactionRequest transactionRequest){
        return ResponseEntity.ok(userService.UpdateUserBalanceAfterDebit(transactionRequest));
    }

}
