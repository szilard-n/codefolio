package com.codefolio.service;

import com.codefolio.dto.auth.AuthResponse;
import com.codefolio.dto.auth.SignInRequest;
import com.codefolio.dto.auth.SignUpRequest;
import com.codefolio.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse signUpUser(SignUpRequest signUpRequest) {
        User newUser = userService.createUser(signUpRequest);
        return new AuthResponse(jwtService.generateToken(newUser));
    }

    public AuthResponse signInUser(SignInRequest signInRequest) {
        final User user = userService.findUserByCredential(signInRequest.credential());

        final UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), signInRequest.password());
        authenticationManager.authenticate(authToken);

        return new AuthResponse(jwtService.generateToken(user));
    }

    protected User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findUserByCredential(((User) auth.getPrincipal()).getUsername());
    }
}
