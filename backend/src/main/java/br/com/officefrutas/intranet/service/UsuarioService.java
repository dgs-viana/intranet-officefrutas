package br.com.officefrutas.intranet.service;

import br.com.officefrutas.intranet.dto.UsuarioCadastroRequest;
import br.com.officefrutas.intranet.dto.UsuarioResponse;
import br.com.officefrutas.intranet.entity.Usuario;
import br.com.officefrutas.intranet.enums.Perfil;
import br.com.officefrutas.intranet.integration.kairos.KairosClient;
import br.com.officefrutas.intranet.integration.kairos.dto.KairosFuncionarioResponse;
import br.com.officefrutas.intranet.repository.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final KairosClient kairosClient;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            KairosClient kairosClient) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.kairosClient = kairosClient;
    }

    public UsuarioResponse cadastrar(
            UsuarioCadastroRequest request) {

        String email = request
                .email()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (usuarioRepository.existsByEmail(email)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe um usuário com este e-mail.");
        }

        if (usuarioRepository.existsByMatriculaKairos(
                request.matriculaKairos())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe um usuário cadastrado para esta matrícula.");
        }

        KairosFuncionarioResponse funcionario = kairosClient.buscarFuncionario(
                request.matriculaKairos());

        Usuario usuario = new Usuario();

        usuario.setMatriculaKairos(
                request.matriculaKairos());

        usuario.setEmail(email);

        usuario.setSenhaHash(
                passwordEncoder.encode(
                        request.senha()));

        usuario.setPerfil(
                Perfil.FUNCIONARIO);

        usuario.setAtivo(true);

        Usuario salvo = usuarioRepository.save(usuario);

        return new UsuarioResponse(

                salvo.getId(),

                salvo.getMatriculaKairos(),

                salvo.getEmail(),

                salvo.getPerfil(),

                salvo.getAtivo(),

                salvo.getDataCriacao(),

                funcionario.nome(),

                funcionario.cargo() != null
                        ? funcionario.cargo().descricao()
                        : null,

                funcionario.estrutura() != null
                        ? funcionario.estrutura().descricao()
                        : null);
    }
}