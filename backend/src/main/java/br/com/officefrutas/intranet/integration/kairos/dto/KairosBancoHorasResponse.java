package br.com.officefrutas.intranet.integration.kairos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KairosBancoHorasResponse(

        @JsonProperty("SaldoAnterior")
        String saldoAnterior,

        @JsonProperty("SaldoPeriodo")
        String saldoPeriodo,

        @JsonProperty("SaldoAtual")
        String saldoAtual

) {
}