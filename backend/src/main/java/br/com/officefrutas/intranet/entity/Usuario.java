package br.com.officefrutas.intranet.entity;

import br.com.officefrutas.intranet.enums.Perfil;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matricula_kairos", nullable = false, unique = true)
    private Integer matriculaKairos;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Perfil perfil;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    public Usuario() {
    }

    @PrePersist
    public void prePersist() {

        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }

        if (ativo == null) {
            ativo = true;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMatriculaKairos() {
        return matriculaKairos;
    }

    public void setMatriculaKairos(Integer matriculaKairos) {
        this.matriculaKairos = matriculaKairos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}