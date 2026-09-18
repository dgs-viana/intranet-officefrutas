package br.com.officefrutas.intranet.service;

import br.com.officefrutas.intranet.dto.UsuarioCadastroRequest;
import br.com.officefrutas.intranet.dto.UsuarioResponse;
import br.com.officefrutas.intranet.entity.Usuario;
import br.com.officefrutas.intranet.enums.Perfil;
import br.com.officefrutas.intranet.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UsuarioResponse cadastrar(UsuarioCadastroRequest request) {

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Já existe um usuário com este e-mail.");
        }

        Usuario usuario = new Usuario();

        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        usuario.setMatricula(request.matricula());
        usuario.setCargo(request.cargo());
        usuario.setDepartamento(request.departamento());

        usuario.setPerfil(Perfil.FUNCIONARIO);
        usuario.setAtivo(true);

        Usuario salvo = usuarioRepository.save(usuario);

        return new UsuarioResponse(
                salvo.getId(),
                salvo.getNome(),
                salvo.getEmail(),
                salvo.getMatricula(),
                salvo.getCargo(),
                salvo.getDepartamento(),
                salvo.getPerfil(),
                salvo.getAtivo()
        );
    }
}