package br.com.officefrutas.intranet.controller;

import br.com.officefrutas.intranet.dto.MeuPerfilResponse;
import br.com.officefrutas.intranet.service.MeuPerfilService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
public class MeuPerfilController {

    private final MeuPerfilService meuPerfilService;

    public MeuPerfilController(
            MeuPerfilService meuPerfilService) {
        this.meuPerfilService = meuPerfilService;
    }

    @GetMapping
    public MeuPerfilResponse buscarMeuPerfil(
            @AuthenticationPrincipal Jwt jwt) {

        Long usuarioId = Long.parseLong(
                jwt.getSubject());

        return meuPerfilService.buscar(
                usuarioId);
    }
}