package br.com.officefrutas.intranet.integration.kairos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KairosResponse<T>(

        @JsonProperty("Sucesso")
        boolean sucesso,

        @JsonProperty("Mensagem")
        String mensagem,

        @JsonProperty("Obj")
        T obj

) {
}