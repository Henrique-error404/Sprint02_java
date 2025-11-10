package com.gestaoestabelecimentos.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EstabelecimentoDTO {

    private Long idEstabelecimento;

    @NotBlank(message = "Nome do estabelecimento é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nmEstabelecimento;

    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(regexp = "\\d{14}", message = "CNPJ deve conter 14 dígitos")
    private String cnpj;

    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    private String enderecoEstabelecimento;

    @Size(max = 15, message = "Telefone deve ter no máximo 15 caracteres")
    private String telEstabelecimento;

    @Size(max = 50, message = "Tipo de estabelecimento deve ter no máximo 50 caracteres")
    private String tpEstabelecimento;

    // Construtores
    public EstabelecimentoDTO() {}

    public EstabelecimentoDTO(String nmEstabelecimento, String cnpj) {
        this.nmEstabelecimento = nmEstabelecimento;
        this.cnpj = cnpj;
    }

    public EstabelecimentoDTO(String nmEstabelecimento, String cnpj, String enderecoEstabelecimento, String telEstabelecimento, String tpEstabelecimento) {
        this.nmEstabelecimento = nmEstabelecimento;
        this.cnpj = cnpj;
        this.enderecoEstabelecimento = enderecoEstabelecimento;
        this.telEstabelecimento = telEstabelecimento;
        this.tpEstabelecimento = tpEstabelecimento;
    }

    // Getters e Setters
    public Long getIdEstabelecimento() {
        return idEstabelecimento;
    }

    public void setIdEstabelecimento(Long idEstabelecimento) {
        this.idEstabelecimento = idEstabelecimento;
    }

    public String getNmEstabelecimento() {
        return nmEstabelecimento;
    }

    public void setNmEstabelecimento(String nmEstabelecimento) {
        this.nmEstabelecimento = nmEstabelecimento;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEnderecoEstabelecimento() {
        return enderecoEstabelecimento;
    }

    public void setEnderecoEstabelecimento(String enderecoEstabelecimento) {
        this.enderecoEstabelecimento = enderecoEstabelecimento;
    }

    public String getTelEstabelecimento() {
        return telEstabelecimento;
    }

    public void setTelEstabelecimento(String telEstabelecimento) {
        this.telEstabelecimento = telEstabelecimento;
    }

    public String getTpEstabelecimento() {
        return tpEstabelecimento;
    }

    public void setTpEstabelecimento(String tpEstabelecimento) {
        this.tpEstabelecimento = tpEstabelecimento;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "EstabelecimentoDTO{" +
                "idEstabelecimento=" + idEstabelecimento +
                ", nmEstabelecimento='" + nmEstabelecimento + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", enderecoEstabelecimento='" + enderecoEstabelecimento + '\'' +
                ", telEstabelecimento='" + telEstabelecimento + '\'' +
                ", tpEstabelecimento='" + tpEstabelecimento + '\'' +
                '}';
    }
}