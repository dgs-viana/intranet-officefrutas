package br.com.officefrutas.intranet.dto;

import br.com.officefrutas.intranet.enums.Perfil;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        String matricula,
        String cargo,
        String departamento,
        Perfil perfil,
        Boolean ativo
) {
}