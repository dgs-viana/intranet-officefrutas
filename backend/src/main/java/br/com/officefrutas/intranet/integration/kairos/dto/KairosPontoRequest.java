package br.com.officefrutas.intranet.integration.kairos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record KairosPontoRequest(

        @JsonProperty("MatriculaPessoa")
        List<Integer> matriculaPessoa,

        @JsonProperty("DataInicio")
        String dataInicio,

        @JsonProperty("DataFim")
        String dataFim,

        @JsonProperty("ResponseType")
        String responseType

) {
}