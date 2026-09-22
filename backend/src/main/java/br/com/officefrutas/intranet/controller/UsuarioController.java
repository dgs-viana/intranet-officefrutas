package br.com.officefrutas.intranet.controller;

import br.com.officefrutas.intranet.dto.UsuarioCadastroRequest;
import br.com.officefrutas.intranet.dto.UsuarioResponse;
import br.com.officefrutas.intranet.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(
            UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse cadastrar(
            @Valid @RequestBody UsuarioCadastroRequest request) {

        return usuarioService.cadastrar(
                request);
    }
}