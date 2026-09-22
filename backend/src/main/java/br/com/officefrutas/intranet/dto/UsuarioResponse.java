package br.com.officefrutas.intranet.dto;

import br.com.officefrutas.intranet.enums.Perfil;

import java.time.LocalDateTime;

public record UsuarioResponse(

                Long id,

                Integer matriculaKairos,

                String email,

                Perfil perfil,

                Boolean ativo,

                LocalDateTime dataCriacao,

                String nome,

                String cargo,

                String estrutura

) {
}