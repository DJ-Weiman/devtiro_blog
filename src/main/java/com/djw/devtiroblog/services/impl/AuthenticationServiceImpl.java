package com.djw.devtiroblog.services.impl;

import com.djw.devtiroblog.domain.dtos.LoginRequest;
import com.djw.devtiroblog.domain.dtos.LoginResponse;
import com.djw.devtiroblog.domain.dtos.RegisterRequest;
import com.djw.devtiroblog.domain.dtos.RegisterResponse;
import com.djw.devtiroblog.domain.entities.User;
import com.djw.devtiroblog.security.BlogUserDetailsService;
import com.djw.devtiroblog.security.jwt.JwtUtils;
import com.djw.devtiroblog.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final BlogUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse signInUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());


        return LoginResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        if(userDetailsService.userExists(registerRequest.getEmail()))
            return null;

//        AuthorityEntity authority = AuthorityEntity.builder()
//                .authorityTitle("ROLE_USER")
//                .build();

        User user = User.builder()
                .name(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userDetailsService.saveUser(user);

        return RegisterResponse.builder()
                .username(savedUser.getName())
                .email(registerRequest.getEmail())
                .build();
    }
}
