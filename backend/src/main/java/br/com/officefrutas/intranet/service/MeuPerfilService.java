package br.com.officefrutas.intranet.service;

import br.com.officefrutas.intranet.dto.MeuPerfilResponse;
import br.com.officefrutas.intranet.entity.Usuario;
import br.com.officefrutas.intranet.integration.kairos.KairosClient;
import br.com.officefrutas.intranet.integration.kairos.dto.KairosFuncionarioResponse;
import br.com.officefrutas.intranet.repository.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MeuPerfilService {

    private final UsuarioRepository usuarioRepository;
    private final KairosClient kairosClient;

    public MeuPerfilService(
            UsuarioRepository usuarioRepository,
            KairosClient kairosClient) {

        this.usuarioRepository = usuarioRepository;
        this.kairosClient = kairosClient;
    }

    public MeuPerfilResponse buscar(
            Long usuarioId) {

        Usuario usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado."));

        KairosFuncionarioResponse funcionario = kairosClient.buscarFuncionario(
                usuario.getMatriculaKairos());

        String cargo = null;

        if (funcionario.cargo() != null) {
            cargo = funcionario
                    .cargo()
                    .descricao();
        }

        String estrutura = null;
        String centroCusto = null;

        if (funcionario.estrutura() != null) {

            estrutura = funcionario
                    .estrutura()
                    .descricao();

            centroCusto = funcionario
                    .estrutura()
                    .centroCusto();
        }

        return new MeuPerfilResponse(

                usuario.getId(),

                usuario.getMatriculaKairos(),

                usuario.getEmail(),

                usuario.getPerfil(),

                usuario.getAtivo(),

                funcionario.nome(),

                cargo,

                estrutura,

                centroCusto,

                funcionario.dataAdmissao());
    }
}