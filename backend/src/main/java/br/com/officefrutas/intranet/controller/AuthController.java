package br.com.officefrutas.intranet.controller;

import br.com.officefrutas.intranet.dto.LoginRequest;
import br.com.officefrutas.intranet.dto.LoginResponse;
import br.com.officefrutas.intranet.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService) {

        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {

        return authService.login(
                request);
    }
}