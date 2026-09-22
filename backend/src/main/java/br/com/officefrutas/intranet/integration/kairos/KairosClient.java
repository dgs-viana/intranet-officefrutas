package br.com.officefrutas.intranet.integration.kairos;

import br.com.officefrutas.intranet.integration.kairos.dto.KairosFuncionarioResponse;
import br.com.officefrutas.intranet.integration.kairos.dto.KairosPessoaRequest;
import br.com.officefrutas.intranet.integration.kairos.dto.KairosPontoRequest;
import br.com.officefrutas.intranet.integration.kairos.dto.KairosRelatorioPontoResponse;
import br.com.officefrutas.intranet.integration.kairos.dto.KairosResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@Component
public class KairosClient {

        private final RestClient restClient;
        private final JsonMapper jsonMapper;

        private final String identifier;
        private final String apiKey;

        public KairosClient(
                        RestClient.Builder builder,
                        JsonMapper jsonMapper,
                        @Value("${kairos.base-url}") String baseUrl,
                        @Value("${kairos.identifier}") String identifier,
                        @Value("${kairos.api-key}") String apiKey) {

                this.restClient = builder
                                .baseUrl(baseUrl)
                                .build();

                this.jsonMapper = jsonMapper;
                this.identifier = identifier;
                this.apiKey = apiKey;
        }

        public KairosFuncionarioResponse buscarFuncionario(
                        Integer matricula) {

                KairosPessoaRequest request = new KairosPessoaRequest(matricula);

                KairosResponse response = executarPost(
                                "/RestServiceApi/People/SearchPerson",
                                request);

                /*
                 * O Kairos retorna:
                 *
                 * Sucesso = false
                 * Mensagem = "Company doesn't have the searched employee"
                 * Obj = null
                 *
                 * quando a matrícula não existe.
                 */
                if (!response.sucesso()) {

                        if (response.mensagem() != null
                                        && response.mensagem()
                                                        .equalsIgnoreCase(
                                                                        "Company doesn't have the searched employee")) {

                                throw new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Funcionário não encontrado no Kairos.");
                        }

                        throw new ResponseStatusException(
                                        HttpStatus.BAD_GATEWAY,
                                        "Erro retornado pelo Kairos: "
                                                        + response.mensagem());
                }

                JsonNode obj = normalizarObj(response);

                if (obj == null) {

                        throw new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Funcionário não encontrado no Kairos.");
                }

                if (obj.isArray()) {

                        if (obj.size() == 0) {

                                throw new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Funcionário não encontrado no Kairos.");
                        }

                        List<KairosFuncionarioResponse> funcionarios = converter(
                                        obj,
                                        new TypeReference<List<KairosFuncionarioResponse>>() {
                                        });

                        if (funcionarios.isEmpty()) {

                                throw new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Funcionário não encontrado no Kairos.");
                        }

                        return funcionarios.getFirst();
                }

                if (obj.isObject()) {

                        return converter(
                                        obj,
                                        new TypeReference<KairosFuncionarioResponse>() {
                                        });
                }

                throw new ResponseStatusException(
                                HttpStatus.BAD_GATEWAY,
                                "Formato inesperado retornado pelo Kairos.");
        }

        public List<KairosRelatorioPontoResponse> buscarPonto(
                        Integer matricula,
                        String dataInicio,
                        String dataFim) {

                KairosPontoRequest request = new KairosPontoRequest(
                                List.of(matricula),
                                dataInicio,
                                dataFim,
                                "AS400V1");

                KairosResponse response = executarPost(
                                "/RestServiceApi/ReportEmployeePunch/GetReportEmployeePunch",
                                request);

                if (!response.sucesso()) {

                        throw new ResponseStatusException(
                                        HttpStatus.BAD_GATEWAY,
                                        "Erro retornado pelo Kairos: "
                                                        + response.mensagem());
                }

                JsonNode obj = normalizarObj(response);

                if (obj == null) {
                        return List.of();
                }

                if (obj.isArray()) {

                        return converter(
                                        obj,
                                        new TypeReference<List<KairosRelatorioPontoResponse>>() {
                                        });
                }

                if (obj.isObject()) {

                        KairosRelatorioPontoResponse relatorio = converter(
                                        obj,
                                        new TypeReference<KairosRelatorioPontoResponse>() {
                                        });

                        return List.of(relatorio);
                }

                throw new ResponseStatusException(
                                HttpStatus.BAD_GATEWAY,
                                "Formato inesperado retornado pelo Kairos.");
        }

        private KairosResponse executarPost(
                        String uri,
                        Object request) {

                try {

                        KairosResponse response = restClient.post()
                                        .uri(uri)
                                        .header(
                                                        "identifier",
                                                        identifier)
                                        .header(
                                                        "key",
                                                        apiKey)
                                        .contentType(
                                                        MediaType.APPLICATION_JSON)
                                        .body(request)
                                        .retrieve()
                                        .body(KairosResponse.class);

                        if (response == null) {

                                throw new ResponseStatusException(
                                                HttpStatus.BAD_GATEWAY,
                                                "O Kairos não retornou uma resposta.");
                        }

                        return response;

                } catch (RestClientResponseException exception) {

                        throw new ResponseStatusException(
                                        HttpStatus.BAD_GATEWAY,
                                        "Erro HTTP ao consultar o Kairos. Status: "
                                                        + exception.getStatusCode(),
                                        exception);
                }
        }

        private JsonNode normalizarObj(
                        KairosResponse response) {

                JsonNode obj = response.obj();

                if (obj == null || obj.isNull()) {
                        return null;
                }

                if (!obj.isString()) {
                        return obj;
                }

                String conteudo = obj.asString();

                if (conteudo == null || conteudo.isBlank()) {
                        return null;
                }

                try {

                        return jsonMapper.readTree(conteudo);

                } catch (JacksonException exception) {

                        throw new ResponseStatusException(
                                        HttpStatus.BAD_GATEWAY,
                                        "Não foi possível interpretar a resposta do Kairos.",
                                        exception);
                }
        }

        private <T> T converter(
                        JsonNode node,
                        TypeReference<T> tipo) {

                try {

                        return jsonMapper.treeToValue(
                                        node,
                                        tipo);

                } catch (JacksonException exception) {

                        throw new ResponseStatusException(
                                        HttpStatus.BAD_GATEWAY,
                                        "Não foi possível converter os dados retornados pelo Kairos.",
                                        exception);
                }
        }
}