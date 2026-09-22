package br.com.officefrutas.intranet.dto;

import br.com.officefrutas.intranet.enums.Perfil;

public record LoginResponse(

        String token,

        String tipo,

        Long expiraEmSegundos,

        Long usuarioId,

        Integer matriculaKairos,

        String email,

        Perfil perfil

) {
}