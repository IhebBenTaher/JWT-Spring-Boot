package com.security.jwt.user;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    public void changePassword(ChangePasswordRequest changePasswordRequest, Principal connectedUser) {
        User user=(User)((UsernamePasswordAuthenticationToken)connectedUser).getPrincipal();
        if(!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())){
            throw new IllegalStateException("wrong password");
        }
        if(!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmationPassword())){
            throw new IllegalStateException("passwords are not the same");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

}
