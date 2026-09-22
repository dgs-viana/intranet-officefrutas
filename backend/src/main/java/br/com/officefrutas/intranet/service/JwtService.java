package br.com.officefrutas.intranet.service;

import br.com.officefrutas.intranet.entity.Usuario;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    private final String issuer;

    private final long expirationSeconds;

    public JwtService(
            JwtEncoder jwtEncoder,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.expiration-seconds}") long expirationSeconds) {

        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.expirationSeconds = expirationSeconds;
    }

    public String gerarToken(
            Usuario usuario) {

        Instant agora = Instant.now();

        Instant expiracao = agora.plusSeconds(
                expirationSeconds);

        JwtClaimsSet claims = JwtClaimsSet.builder()

                .issuer(issuer)

                .issuedAt(agora)

                .expiresAt(expiracao)

                .subject(
                        usuario
                                .getId()
                                .toString())

                .claim(
                        "usuarioId",
                        usuario.getId())

                .claim(
                        "matricula",
                        usuario
                                .getMatriculaKairos())

                .claim(
                        "email",
                        usuario.getEmail())

                .claim(
                        "perfil",
                        usuario
                                .getPerfil()
                                .name())

                .build();

        return jwtEncoder
                .encode(
                        JwtEncoderParameters
                                .from(claims))
                .getTokenValue();
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }
}