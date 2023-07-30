package com.IdentityRegistry.IdentityRegistry.service.serviceImpl;

import com.IdentityRegistry.IdentityRegistry.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.IdentityRegistry.IdentityRegistry.entity.User user = userRepository.findByEmail(email);
//                .orElseThrow(() -> new UsernameNotFoundException("User with provided credentials not found!" + email));

        //retrieve roles associated with the user
        //granted authority is what we used for mapping a role to a user
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
