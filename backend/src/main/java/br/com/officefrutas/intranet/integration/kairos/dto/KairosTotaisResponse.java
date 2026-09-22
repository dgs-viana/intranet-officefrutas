package br.com.officefrutas.intranet.integration.kairos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KairosTotaisResponse(

        @JsonProperty("TotalHorasTrabalhadas")
        String horasTrabalhadas,

        @JsonProperty("TotalHorasExtraordinarias")
        String horasExtras

) {
}