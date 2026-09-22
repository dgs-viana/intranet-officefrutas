package br.com.officefrutas.intranet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {

        http

                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/auth/login",
                                "/error")
                        .permitAll()

                        /*
                         * TEMPORÁRIO.
                         *
                         * Enquanto estamos desenvolvendo com H2,
                         * precisamos conseguir criar o primeiro usuário.
                         *
                         * Depois vamos retirar isso e deixar
                         * cadastro de usuários para ADMIN/RH.
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/usuarios")
                        .permitAll()

                        /*
                         * Qualquer usuário autenticado pode
                         * consultar os próprios dados.
                         */
                        .requestMatchers(
                                "/api/me/**")
                        .authenticated()

                        /*
                         * Endpoints administrativos do Kairos.
                         *
                         * Funcionário comum não pode informar
                         * uma matrícula qualquer e consultar
                         * dados de outra pessoa.
                         */
                        .requestMatchers(
                                "/api/kairos/**")
                        .hasAnyRole(
                                "RH",
                                "ADMIN")

                        .anyRequest()
                        .authenticated())

                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(
                        jwtAuthenticationConverter)));

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName(
                "perfil");

        authoritiesConverter.setAuthorityPrefix(
                "ROLE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(
                authoritiesConverter);

        return converter;
    }
}