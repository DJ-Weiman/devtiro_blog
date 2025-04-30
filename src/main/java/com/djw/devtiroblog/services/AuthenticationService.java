package com.djw.devtiroblog.services;

import com.djw.devtiroblog.domain.dtos.LoginRequest;
import com.djw.devtiroblog.domain.dtos.LoginResponse;
import com.djw.devtiroblog.domain.dtos.RegisterRequest;
import com.djw.devtiroblog.domain.dtos.RegisterResponse;

import javax.naming.AuthenticationException;

public interface AuthenticationService {
    LoginResponse signInUser(LoginRequest loginRequest);
    RegisterResponse registerUser(RegisterRequest registerRequest);
}
