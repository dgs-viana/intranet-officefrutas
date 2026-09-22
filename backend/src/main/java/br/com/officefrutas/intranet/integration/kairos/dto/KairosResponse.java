package br.com.officefrutas.intranet.integration.kairos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KairosResponse(

                @JsonProperty("Sucesso") boolean sucesso,

                @JsonProperty("Mensagem") String mensagem,

                @JsonProperty("Obj") JsonNode obj

) {
}