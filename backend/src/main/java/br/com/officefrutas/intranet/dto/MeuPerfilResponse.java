package br.com.officefrutas.intranet.dto;

import br.com.officefrutas.intranet.enums.Perfil;

public record MeuPerfilResponse(

        Long id,

        Integer matriculaKairos,

        String email,

        Perfil perfil,

        Boolean ativo,

        String nome,

        String cargo,

        String estrutura,

        String centroCusto,

        String dataAdmissao

) {
}