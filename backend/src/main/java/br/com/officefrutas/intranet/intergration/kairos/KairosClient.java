package br.com.officefrutas.intranet.integration.kairos;

import br.com.officefrutas.intranet.integration.kairos.dto.KairosPontoRequest;
import br.com.officefrutas.intranet.integration.kairos.dto.KairosRelatorioPontoResponse;
import br.com.officefrutas.intranet.integration.kairos.dto.KairosResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class KairosClient {

    private final RestClient restClient;
    private final String identifier;
    private final String apiKey;

    public KairosClient(
            RestClient.Builder builder,
            @Value("${kairos.base-url}") String baseUrl,
            @Value("${kairos.identifier}") String identifier,
            @Value("${kairos.api-key}") String apiKey) {

        this.restClient = builder
                .baseUrl(baseUrl)
                .build();

        this.identifier = identifier;
        this.apiKey = apiKey;
    }

    public List<KairosRelatorioPontoResponse> buscarPonto(
            Integer matricula,
            String dataInicio,
            String dataFim) {

        KairosPontoRequest request = new KairosPontoRequest(
                List.of(matricula),
                dataInicio,
                dataFim,
                "AS400V1"
        );

        KairosResponse<List<KairosRelatorioPontoResponse>> response =
                restClient.post()
                        .uri("/RestServiceApi/ReportEmployeePunch/GetReportEmployeePunch")
                        .header("identifier", identifier)
                        .header("key", apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(request)
                        .retrieve()
                        .body(new ParameterizedTypeReference<>() {
                        });

        if (response == null) {
            throw new IllegalStateException(
                    "O Kairos não retornou uma resposta."
            );
        }

        if (!response.sucesso()) {
            throw new IllegalStateException(
                    "Erro ao consultar Kairos: " + response.mensagem()
            );
        }

        return response.obj();
    }
}