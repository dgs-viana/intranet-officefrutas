package br.com.officefrutas.intranet.integration.kairos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KairosEntradaResponse(

        @JsonProperty("Data")
        String data,

        @JsonProperty("Horario")
        String horario,

        @JsonProperty("Apontamentos")
        String apontamentos,

        @JsonProperty("HTrab")
        String horasTrabalhadas,

        @JsonProperty("HE")
        String horasExtras,

        @JsonProperty("Descontos")
        String descontos,

        @JsonProperty("Justificativa")
        String justificativa,

        @JsonProperty("Status")
        String status

) {
}