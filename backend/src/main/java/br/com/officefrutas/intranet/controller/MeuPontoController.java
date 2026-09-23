package br.com.officefrutas.intranet.controller;

import br.com.officefrutas.intranet.integration.kairos.dto.KairosRelatorioPontoResponse;
import br.com.officefrutas.intranet.service.MeuPontoService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/me/ponto")
public class MeuPontoController {

    private final MeuPontoService meuPontoService;

    public MeuPontoController(
            MeuPontoService meuPontoService) {
        this.meuPontoService = meuPontoService;
    }

    @GetMapping
    public List<KairosRelatorioPontoResponse> buscarPonto(

            @AuthenticationPrincipal Jwt jwt,

            @RequestParam String dataInicio,

            @RequestParam String dataFim) {

        Long usuarioId = Long.parseLong(
                jwt.getSubject());

        return meuPontoService.buscarPonto(
                usuarioId,
                dataInicio,
                dataFim);
    }
}