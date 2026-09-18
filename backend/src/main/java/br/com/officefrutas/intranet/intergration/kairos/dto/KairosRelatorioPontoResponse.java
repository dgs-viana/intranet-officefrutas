package br.com.officefrutas.intranet.integration.kairos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record KairosRelatorioPontoResponse(

        @JsonProperty("InfoFuncionario")
        KairosFuncionarioResponse funcionario,

        @JsonProperty("Entradas")
        List<KairosEntradaResponse> entradas,

        @JsonProperty("Totais")
        KairosTotaisResponse totais,

        @JsonProperty("BancoHorasSaldo")
        KairosBancoHorasResponse bancoHoras

) {
}