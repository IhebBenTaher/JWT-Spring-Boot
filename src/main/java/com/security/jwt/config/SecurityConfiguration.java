package com.security.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.security.jwt.user.Permission;
import com.security.jwt.user.Role;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    // @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(req->req.requestMatchers("/api/v1/auth/**").permitAll()
        .requestMatchers("/api/v1/management/**").hasAnyRole(Role.ADMIN.name(),Role.MANAGER.name())
        .requestMatchers(HttpMethod.GET,"/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_READ.name(),Permission.MANAGER_READ.name())
        .requestMatchers(HttpMethod.POST,"/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_CREATE.name(),Permission.MANAGER_CREATE.name())
        .requestMatchers(HttpMethod.PUT,"/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_UPDATE.name(),Permission.MANAGER_UPDATE.name())
        .requestMatchers(HttpMethod.DELETE,"/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_DELETE.name(),Permission.MANAGER_DELETE.name())
        .requestMatchers("/api/v1/admin/**").hasRole(Role.ADMIN.name())
        .requestMatchers(HttpMethod.GET,"/api/v1/admin/**").hasAuthority(Permission.ADMIN_READ.name())
        .requestMatchers(HttpMethod.POST,"/api/v1/admin/**").hasAuthority(Permission.ADMIN_CREATE.name())
        .requestMatchers(HttpMethod.PUT,"/api/v1/admin/**").hasAuthority(Permission.ADMIN_UPDATE.name())
        .requestMatchers(HttpMethod.DELETE,"/api/v1/admin/**").hasAuthority(Permission.ADMIN_DELETE.name())
        .anyRequest().authenticated())
        .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .logout().logoutUrl("/api/v1/auth/logout").addLogoutHandler(logoutHandler).logoutSuccessHandler((request,response,authentication)->SecurityContextHolder.clearContext());
        return http.build();
    }
}
