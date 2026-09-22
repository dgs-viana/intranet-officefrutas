package br.com.officefrutas.intranet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Bean
    public SecretKey jwtSecretKey(
            @Value("${jwt.secret:}") String secret) {

        if (secret != null && !secret.isBlank()) {

            try {

                byte[] chave = Base64.getDecoder()
                        .decode(secret);

                if (chave.length < 32) {

                    throw new IllegalArgumentException(
                            "JWT_SECRET precisa ter pelo menos 256 bits.");
                }

                return new SecretKeySpec(
                        chave,
                        "HmacSHA256");

            } catch (IllegalArgumentException exception) {

                throw new IllegalStateException(
                        "JWT_SECRET inválido. "
                                + "Utilize uma chave Base64 válida.",
                        exception);
            }
        }

        try {

            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                    "HmacSHA256");

            keyGenerator.init(256);

            return keyGenerator.generateKey();

        } catch (NoSuchAlgorithmException exception) {

            throw new IllegalStateException(
                    "Não foi possível gerar "
                            + "a chave JWT.",
                    exception);
        }
    }

    @Bean
    public JwtEncoder jwtEncoder(
            SecretKey secretKey) {

        return NimbusJwtEncoder
                .withSecretKey(secretKey)
                .algorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(
            SecretKey secretKey,
            @Value("${jwt.issuer}") String issuer) {

        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

        decoder.setJwtValidator(
                JwtValidators
                        .createDefaultWithIssuer(
                                issuer));

        return decoder;
    }
}