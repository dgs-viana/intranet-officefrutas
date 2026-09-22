package br.com.officefrutas.intranet.integration.kairos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KairosCargoResponse(

        @JsonProperty("Id") Integer id,

        @JsonProperty("Codigo") Integer codigo,

        @JsonProperty("Descricao") String descricao,

        @JsonProperty("Extra1") String extra1,

        @JsonProperty("Extra2") String extra2

) {
}