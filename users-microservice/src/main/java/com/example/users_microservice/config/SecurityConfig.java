package com.example.users_microservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
@EnableMethodSecurity
public class SecurityConfig {

  private static final String[] WHITE_LIST = {"/swagger-ui.html", "/v3/**", "/swagger-ui/**", "/csrf"};

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // CSRF
    return http
        .csrf(AbstractHttpConfigurer::disable)
//                .csrf(csrf ->
//                        csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
//                )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(WHITE_LIST).permitAll()
            .requestMatchers(HttpMethod.POST, "/v1/users").permitAll()
            .requestMatchers(HttpMethod.DELETE, "/v1/users/*").hasAuthority("ADMIN")
            .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults())
        .build();
  }

}
