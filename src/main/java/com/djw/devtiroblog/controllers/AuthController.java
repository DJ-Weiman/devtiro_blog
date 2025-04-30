package com.djw.devtiroblog.controllers;

import com.djw.devtiroblog.domain.dtos.LoginRequest;
import com.djw.devtiroblog.domain.dtos.LoginResponse;
import com.djw.devtiroblog.domain.dtos.RegisterRequest;
import com.djw.devtiroblog.domain.dtos.RegisterResponse;
import com.djw.devtiroblog.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse;
        loginResponse = authService.signInUser(loginRequest);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/registerUser")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest){
        RegisterResponse registerResponse = authService.registerUser(registerRequest);

        if(registerResponse == null){
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad Credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(registerResponse);
    }
}
