package com.IdentityRegistry.IdentityRegistry.security;

import com.IdentityRegistry.IdentityRegistry.service.serviceImpl.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private CustomUserDetailsService customUserDetailsService;
    private JwtAuthenticationEntryPoint authenticationEntrypoint;
    private JwtAuthenticationFilter authenticationFilter;
    private final LogoutHandler logoutHandler;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();

    }

//    @Bean
//    public AuthenticationProvider authenticationProvider(){
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider();
//    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())

                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling.authenticationEntryPoint(authenticationEntrypoint)
                )

                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers("api/v1/auth/**").permitAll()
                            .requestMatchers("api/v1/role/**").permitAll()
                            .requestMatchers("api/users/**").permitAll()
                            .requestMatchers("api/v1/transaction/**").permitAll()
                            .anyRequest().permitAll();
                })

//                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .logout((logout) ->
                        logout.deleteCookies("remove")
                                .invalidateHttpSession(false)
                                .logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) ->
                                        SecurityContextHolder.clearContext())
                )

                .httpBasic(Customizer.withDefaults());


        return httpSecurity.build();
    }

}
