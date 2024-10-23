package com.security.jwt.auth;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.jwt.config.JwtService;
import com.security.jwt.token.Token;
import com.security.jwt.token.TokenRepository;
import com.security.jwt.token.TokenType;
import com.security.jwt.user.User;
import com.security.jwt.user.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        User user=User.builder().firstname(request.getFirstname()).lastname(request.getLastname())
        .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).role(request.getRole()).build();
        User user2= userRepository.save(user);
        String jwtToken=jwtService.generateToken(user);
        String refreshToken=jwtService.generateRefreshToken(user);
        saveUserToken(user2, jwtToken);
        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }
    private void saveUserToken(User user,String jwtToken){
        Token token=Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER).expired(false).revoked(false).build();
        tokenRepository.save(token);
    }
    private void revokeAllUserTokens(User user){
        List<Token>validTokens=tokenRepository.findAllValidTokensByUser(user.getId());
        if(validTokens.isEmpty()){
            return;
        }
        validTokens.forEach(t->{
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        User user=userRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken=jwtService.generateToken(user);
        String refreshToken=jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if(authHeader==null||!authHeader.startsWith("Bearer ")){
            return;
        }
        refreshToken=authHeader.substring(7);
        userEmail=jwtService.extractUsername(refreshToken);
        if(userEmail!=null){
            var userDetails=userRepository.findByEmail(userEmail).orElseThrow();
            if(jwtService.isTokenValid(refreshToken, userDetails)){
                String accessToken=jwtService.generateToken(userDetails);
                revokeAllUserTokens(userDetails);
                saveUserToken(userDetails, accessToken);
                AuthenticationResponse authResponse=AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
                new ObjectMapper().writeValue(response.getOutputStream(),authResponse);
            }
        }
    }
}
