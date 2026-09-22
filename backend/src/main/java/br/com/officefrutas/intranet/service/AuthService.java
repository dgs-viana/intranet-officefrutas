package br.com.officefrutas.intranet.service;

import br.com.officefrutas.intranet.dto.LoginRequest;
import br.com.officefrutas.intranet.dto.LoginResponse;
import br.com.officefrutas.intranet.entity.Usuario;
import br.com.officefrutas.intranet.repository.UsuarioRepository;

import org.springframework.http.HttpStatus;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;

        this.passwordEncoder = passwordEncoder;

        this.jwtService = jwtService;
    }

    public LoginResponse login(
            LoginRequest request) {

        String email = request
                .email()
                .trim()
                .toLowerCase(
                        Locale.ROOT);

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "E-mail ou senha inválidos."));

        if (!Boolean.TRUE.equals(
                usuario.getAtivo())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Usuário inativo.");
        }

        boolean senhaCorreta = passwordEncoder.matches(
                request.senha(),
                usuario.getSenhaHash());

        if (!senhaCorreta) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "E-mail ou senha inválidos.");
        }

        String token = jwtService.gerarToken(
                usuario);

        return new LoginResponse(

                token,

                "Bearer",

                jwtService
                        .getExpirationSeconds(),

                usuario.getId(),

                usuario.getMatriculaKairos(),

                usuario.getEmail(),

                usuario.getPerfil());
    }
}