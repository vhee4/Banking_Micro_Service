package com.IdentityRegistry.IdentityRegistry.service.serviceImpl;
import com.IdentityRegistry.IdentityRegistry.dto.TransactionRequest;
import com.IdentityRegistry.IdentityRegistry.dto.UserData;
import com.IdentityRegistry.IdentityRegistry.dto.UserResponse;
import com.IdentityRegistry.IdentityRegistry.entity.User;
import com.IdentityRegistry.IdentityRegistry.entity.UserStatus;
import com.IdentityRegistry.IdentityRegistry.repository.RoleRepository;
import com.IdentityRegistry.IdentityRegistry.repository.UserRepository;
import com.IdentityRegistry.IdentityRegistry.service.serviceInterface.UserService;
import com.IdentityRegistry.IdentityRegistry.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final WebClient webClient;

    @Override
    public List<UserResponse> allUsers(){
        List<User> userslist = userRepository.findAll();
        return userslist.stream()
                .filter(users->users.getStatus() == UserStatus.ACTIVE)
                .map(users->UserResponse.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .userData(UserData.builder()
                        .accountBalance(users.getAccountBalance())
                        .accountNumber(users.getAccountNumber())
                        .accountName(users.getFirstName() + " " + users.getLastName() + " " + users.getOtherName())
                        .build())
                .build()).toList();
    }
    //write a method for get all users, get all active users & get all inactive users

@Override
    public UserResponse fetchUser(Long userId){
        if(!userRepository.existsById(userId)){
            return UserResponse.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .userData(null)
                    .build();
    }
   User user = userRepository.findById(userId).get();
           return UserResponse.builder()
                   .responseCode(ResponseUtils.SUCCESS)
                   .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                   .userData(UserData.builder()
                           .accountBalance(user.getAccountBalance())
                           .accountNumber(user.getAccountNumber())
                           .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                           .build()).
                   build();
    }

    @Override
   public UserResponse balanceEnquiry(String accountNumber){
        boolean isAccountExist = userRepository.existsByAccountNumber(accountNumber);
        if(!isAccountExist){
            return UserResponse.builder()
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .userData(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(accountNumber);
        return UserResponse.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .userData(UserData.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build();
    }

    public UserResponse nameEnquiry(String accountNumber){
        boolean isAccountExist = userRepository.existsByAccountNumber(accountNumber);
        if(!isAccountExist){
            return UserResponse.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .userData(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(accountNumber);
        return UserResponse.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .userData(UserData.builder()
                        .accountBalance(null)
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(null)
                        .build())
                .build();
    }
    public UserResponse deleteUser(String accountNumber){  //do a soft delete nad also set its token to expire immediately
        User deleteduser = userRepository.findByAccountNumber(accountNumber);
        deleteduser.setStatus(UserStatus.INACTIVE);
        log.info("you have set the status");
//        Roles role = roleRepository.findByRoleName("ROLE_DELETED-USER").orElseThrow();
//        deleteduser.setRoles(Collections.singleton(role));
//        log.info("you have set the role");

        userRepository.save(deleteduser);
        log.info("you have saved the entity");
        return UserResponse.builder()
                .responseCode(ResponseUtils.USER_DELETED_CODE)
                .responseMessage(ResponseUtils.USER_DELETED_MESSAGE)
                .build();

    }

    @Override
    public UserResponse UpdateUserBalanceAfterCredit(TransactionRequest transactionRequest) {
        User user = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());
        user.setAccountBalance(user.getAccountBalance().add(transactionRequest.getAmount()));
        userRepository.save(user);

        return UserResponse.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION_CODE)
                .responseMessage(ResponseUtils.ACCOUNT_CREDITED)
                .userData(UserData.builder()
                        .accountName(user.getFirstName() + " " + user.getLastName())
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public UserResponse UpdateUserBalanceAfterDebit(TransactionRequest transactionRequest) {
        User user = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());
        user.setAccountBalance(user.getAccountBalance().subtract(transactionRequest.getAmount()));
        userRepository.save(user);

        return UserResponse.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION_CODE)
                .responseMessage(ResponseUtils.ACCOUNT_DEBITED)
                .userData(UserData.builder()
                        .accountName(user.getFirstName() + " " + user.getLastName())
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build();
    }



}
