package br.com.officefrutas.intranet.integration.kairos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KairosFuncionarioResponse(

        @JsonProperty("Nome")
        String nome,

        @JsonProperty("Matricula")
        Integer matricula,

        @JsonProperty("Estrutura")
        String estrutura,

        @JsonProperty("Cargo")
        String cargo

) {
}