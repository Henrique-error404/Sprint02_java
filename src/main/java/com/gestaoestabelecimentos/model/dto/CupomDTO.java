package com.gestaoestabelecimentos.model.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CupomDTO {

    private Long idCupom;

    @NotNull(message = "Valor do cupom é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor do cupom deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Valor do cupom deve ter no máximo 2 casas decimais")
    private BigDecimal vlCupom;

    private LocalDateTime dtCriacao;

    private LocalDateTime dtValidade;

    private Boolean utilizado;

    @NotNull(message = "Usuário é obrigatório")
    private Long idUsuario;

    // Construtores
    public CupomDTO() {}

    public CupomDTO(BigDecimal vlCupom, Long idUsuario) {
        this.vlCupom = vlCupom;
        this.idUsuario = idUsuario;
        this.utilizado = false;
    }

    public CupomDTO(BigDecimal vlCupom, LocalDateTime dtValidade, Long idUsuario) {
        this.vlCupom = vlCupom;
        this.dtValidade = dtValidade;
        this.idUsuario = idUsuario;
        this.utilizado = false;
    }

    // Getters e Setters
    public Long getIdCupom() {
        return idCupom;
    }

    public void setIdCupom(Long idCupom) {
        this.idCupom = idCupom;
    }

    public BigDecimal getVlCupom() {
        return vlCupom;
    }

    public void setVlCupom(BigDecimal vlCupom) {
        this.vlCupom = vlCupom;
    }

    public LocalDateTime getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(LocalDateTime dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public LocalDateTime getDtValidade() {
        return dtValidade;
    }

    public void setDtValidade(LocalDateTime dtValidade) {
        this.dtValidade = dtValidade;
    }

    public Boolean getUtilizado() {
        return utilizado;
    }

    public void setUtilizado(Boolean utilizado) {
        this.utilizado = utilizado;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}