package com.gestaoestabelecimentos.model.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ProdutoDTO {

    private Long idProduto;

    @NotBlank(message = "Nome do produto é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nmProduto;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String dsProduto;

    @NotNull(message = "Valor do produto é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Valor deve ter no máximo 2 casas decimais")
    private BigDecimal vlProduto;

    @Future(message = "Data de validade deve ser futura")
    private LocalDate dtValidade;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 0, message = "Quantidade não pode ser negativa")
    private Integer qtProduto;

    @NotNull(message = "Estabelecimento é obrigatório")
    private Long idEstabelecimento;

    // Construtores
    public ProdutoDTO() {}

    public ProdutoDTO(String nmProduto, BigDecimal vlProduto, Integer qtProduto, Long idEstabelecimento) {
        this.nmProduto = nmProduto;
        this.vlProduto = vlProduto;
        this.qtProduto = qtProduto;
        this.idEstabelecimento = idEstabelecimento;
    }

    public ProdutoDTO(String nmProduto, String dsProduto, BigDecimal vlProduto, LocalDate dtValidade, Integer qtProduto, Long idEstabelecimento) {
        this.nmProduto = nmProduto;
        this.dsProduto = dsProduto;
        this.vlProduto = vlProduto;
        this.dtValidade = dtValidade;
        this.qtProduto = qtProduto;
        this.idEstabelecimento = idEstabelecimento;
    }

    // Getters e Setters
    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getNmProduto() {
        return nmProduto;
    }

    public void setNmProduto(String nmProduto) {
        this.nmProduto = nmProduto;
    }

    public String getDsProduto() {
        return dsProduto;
    }

    public void setDsProduto(String dsProduto) {
        this.dsProduto = dsProduto;
    }

    public BigDecimal getVlProduto() {
        return vlProduto;
    }

    public void setVlProduto(BigDecimal vlProduto) {
        this.vlProduto = vlProduto;
    }

    public LocalDate getDtValidade() {
        return dtValidade;
    }

    public void setDtValidade(LocalDate dtValidade) {
        this.dtValidade = dtValidade;
    }

    public Integer getQtProduto() {
        return qtProduto;
    }

    public void setQtProduto(Integer qtProduto) {
        this.qtProduto = qtProduto;
    }

    public Long getIdEstabelecimento() {
        return idEstabelecimento;
    }

    public void setIdEstabelecimento(Long idEstabelecimento) {
        this.idEstabelecimento = idEstabelecimento;
    }
}