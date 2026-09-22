package br.com.officefrutas.intranet.integration.kairos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KairosFuncionarioResponse(

                @JsonProperty("Nome") String nome,

                @JsonProperty("Matricula") Integer matricula,

                @JsonProperty("Estrutura") KairosEstruturaResponse estrutura,

                @JsonProperty("Cargo") KairosCargoResponse cargo,

                @JsonProperty("DataAdmissao") String dataAdmissao,

                @JsonProperty("DataDemissao") String dataDemissao

) {
}