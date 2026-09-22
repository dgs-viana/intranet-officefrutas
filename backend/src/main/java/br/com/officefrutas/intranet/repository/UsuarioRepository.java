package br.com.officefrutas.intranet.repository;

import br.com.officefrutas.intranet.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByMatriculaKairos(
            Integer matriculaKairos);

    boolean existsByEmail(String email);

    boolean existsByMatriculaKairos(
            Integer matriculaKairos);
}