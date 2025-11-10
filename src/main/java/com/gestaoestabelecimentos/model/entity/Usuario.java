package com.gestaoestabelecimentos.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "T_USUARIO",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email", name = "uk_usuario_email")
        }
)
@SequenceGenerator(
        name = "seq_usuario",
        sequenceName = "SQ_T_USUARIO",
        allocationSize = 1
)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @Column(name = "id_usuario")
    private Long idUsuario;

    @NotBlank(message = "Nome do usuário é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    @Column(name = "nm_usuario", length = 100, nullable = false)
    private String nmUsuario;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, max = 255, message = "Senha deve ter entre 6 e 255 caracteres")
    @Column(name = "senha", length = 255, nullable = false)
    private String senha;

    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    @Column(name = "endereco_usuario", length = 200)
    private String enderecoUsuario;

    @Size(max = 15, message = "Telefone deve ter no máximo 15 caracteres")
    @Column(name = "tel_usuario", length = 15)
    private String telUsuario;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cupom> cupons = new ArrayList<>();

    // Construtores
    public Usuario() {}

    public Usuario(String nmUsuario, String email, String senha) {
        this.nmUsuario = nmUsuario;
        this.email = email;
        this.senha = senha;
    }

    public Usuario(String nmUsuario, String email, String senha, String enderecoUsuario, String telUsuario) {
        this.nmUsuario = nmUsuario;
        this.email = email;
        this.senha = senha;
        this.enderecoUsuario = enderecoUsuario;
        this.telUsuario = telUsuario;
    }

    // Getters e Setters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNmUsuario() {
        return nmUsuario;
    }

    public void setNmUsuario(String nmUsuario) {
        this.nmUsuario = nmUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEnderecoUsuario() {
        return enderecoUsuario;
    }

    public void setEnderecoUsuario(String enderecoUsuario) {
        this.enderecoUsuario = enderecoUsuario;
    }

    public String getTelUsuario() {
        return telUsuario;
    }

    public void setTelUsuario(String telUsuario) {
        this.telUsuario = telUsuario;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public List<Cupom> getCupons() {
        return cupons;
    }

    public void setCupons(List<Cupom> cupons) {
        this.cupons = cupons;
    }

    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(idUsuario, usuario.idUsuario) &&
                Objects.equals(email, usuario.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, email);
    }

    // toString
    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nmUsuario='" + nmUsuario + '\'' +
                ", email='" + email + '\'' +
                ", enderecoUsuario='" + enderecoUsuario + '\'' +
                ", telUsuario='" + telUsuario + '\'' +
                '}';
    }
}