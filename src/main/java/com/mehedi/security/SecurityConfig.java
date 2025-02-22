package com.mehedi.security;

import com.mehedi.constatnts.AppConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager)
            throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->{
                    auth
                            .requestMatchers(HttpMethod.POST, AppConstants.SIGN_IN,AppConstants.SIGN_UP).permitAll()

                            .requestMatchers(HttpMethod.GET,"/users/{userId}").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET,"/users/{userId}/books").authenticated()
                            .requestMatchers(HttpMethod.GET,"/users/{userId}/borrowed-books").authenticated()

                            .requestMatchers(HttpMethod.POST,"/books/create").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT,"/books/update").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE,"/books/delete").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET,"books/all").authenticated()

//                            .requestMatchers(HttpMethod.POST,"/books/{bookId}/borrow").hasAnyRole("CUSTOMER", "ADMIN")
                            .requestMatchers(HttpMethod.POST,"/books/{bookId}/borrow").hasRole("CUSTOMER")
                            .requestMatchers(HttpMethod.PUT,"/books/{bookId}/return").hasRole("CUSTOMER")
                            .requestMatchers(HttpMethod.POST,"/books/{bookId}/reserve").authenticated()
                            .requestMatchers(HttpMethod.PUT,"/books/{bookId}/cancel-reservation").authenticated()

                            .requestMatchers(HttpMethod.GET,"/books/{bookId}/reviews").authenticated()
                            .requestMatchers(HttpMethod.POST,"/books/{bookId}/reviews/create").authenticated()
                            .requestMatchers(HttpMethod.PUT,"/books/{bookId}/reviews/{reviewId}/update").authenticated()
                            .requestMatchers(HttpMethod.DELETE,"/books/{bookId}/reviews/{reviewId}/delete").authenticated()

                            .requestMatchers(HttpMethod.GET,"/users/{userId}/history").authenticated()
                            .anyRequest().permitAll();
                })
                .addFilter(new CustomAuthenticationFilter(authenticationManager))
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }
}