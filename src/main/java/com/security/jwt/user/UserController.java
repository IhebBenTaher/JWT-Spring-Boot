package com.security.jwt.user;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    @PatchMapping
    public ResponseEntity<?>changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,Principal connectedUser){
        userService.changePassword(changePasswordRequest,connectedUser);
        return ResponseEntity.ok().build();
    }
}
