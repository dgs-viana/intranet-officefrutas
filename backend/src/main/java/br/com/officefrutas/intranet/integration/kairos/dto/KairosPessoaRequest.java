package br.com.officefrutas.intranet.integration.kairos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KairosPessoaRequest(

        @JsonProperty("Matricula") Integer matricula

) {
}