package br.com.officefrutas.intranet.controller;

import br.com.officefrutas.intranet.integration.kairos.KairosClient;
import br.com.officefrutas.intranet.integration.kairos.dto.KairosFuncionarioResponse;
import br.com.officefrutas.intranet.integration.kairos.dto.KairosRelatorioPontoResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/kairos")
public class KairosController {

    private final KairosClient kairosClient;

    public KairosController(
            KairosClient kairosClient) {
        this.kairosClient = kairosClient;
    }

    @GetMapping("/funcionario/{matricula}")
    public KairosFuncionarioResponse buscarFuncionario(
            @PathVariable Integer matricula) {

        return kairosClient.buscarFuncionario(
                matricula);
    }

    @GetMapping("/ponto/{matricula}")
    public List<KairosRelatorioPontoResponse> buscarPonto(

            @PathVariable Integer matricula,

            @RequestParam String dataInicio,

            @RequestParam String dataFim

    ) {

        return kairosClient.buscarPonto(
                matricula,
                dataInicio,
                dataFim);
    }
}