package br.com.officefrutas.intranet.service;

import br.com.officefrutas.intranet.entity.Usuario;
import br.com.officefrutas.intranet.integration.kairos.KairosClient;
import br.com.officefrutas.intranet.integration.kairos.dto.KairosRelatorioPontoResponse;
import br.com.officefrutas.intranet.repository.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class MeuPontoService {

    private final UsuarioRepository usuarioRepository;
    private final KairosClient kairosClient;

    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MeuPontoService(
            UsuarioRepository usuarioRepository,
            KairosClient kairosClient) {
        this.usuarioRepository = usuarioRepository;
        this.kairosClient = kairosClient;
    }

    public List<KairosRelatorioPontoResponse> buscarPonto(
            Long usuarioId,
            String dataInicio,
            String dataFim) {

        validarPeriodo(dataInicio, dataFim);

        Usuario usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado."));

        if (!Boolean.TRUE.equals(usuario.getAtivo())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Usuário inativo.");
        }

        return kairosClient.buscarPonto(
                usuario.getMatriculaKairos(),
                dataInicio,
                dataFim);
    }

    private void validarPeriodo(
            String dataInicio,
            String dataFim) {

        LocalDate inicio;
        LocalDate fim;

        try {
            inicio = LocalDate.parse(
                    dataInicio,
                    FORMATADOR_DATA);

            fim = LocalDate.parse(
                    dataFim,
                    FORMATADOR_DATA);

        } catch (DateTimeParseException exception) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "As datas devem estar no formato dd/MM/yyyy.");
        }

        if (inicio.isAfter(fim)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A data inicial não pode ser maior que a data final.");
        }
    }
}