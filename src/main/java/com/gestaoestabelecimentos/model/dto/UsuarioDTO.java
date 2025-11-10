package com.gestaoestabelecimentos.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioDTO {

    private Long idUsuario;

    @NotBlank(message = "Nome do usuário é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nmUsuario;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, max = 255, message = "Senha deve ter entre 6 e 255 caracteres")
    private String senha;

    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    private String enderecoUsuario;

    @Size(max = 15, message = "Telefone deve ter no máximo 15 caracteres")
    private String telUsuario;

    // Construtores
    public UsuarioDTO() {}

    public UsuarioDTO(String nmUsuario, String email, String senha) {
        this.nmUsuario = nmUsuario;
        this.email = email;
        this.senha = senha;
    }

    public UsuarioDTO(String nmUsuario, String email, String senha, String enderecoUsuario, String telUsuario) {
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
}