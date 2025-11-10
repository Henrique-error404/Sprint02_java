package com.gestaoestabelecimentos.model.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class PagamentoDTO {

    private Long idPagamento;

    @NotBlank(message = "Forma de pagamento é obrigatória")
    @Size(max = 50, message = "Forma de pagamento deve ter no máximo 50 caracteres")
    private String formaPagamento;

    @NotNull(message = "Valor do pagamento é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor do pagamento deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Valor do pagamento deve ter no máximo 2 casas decimais")
    private BigDecimal vlPagamento;

    @NotNull(message = "Status do pagamento é obrigatório")
    @Min(value = 0, message = "Status deve ser 0 ou 1")
    @Max(value = 1, message = "Status deve ser 0 ou 1")
    private Integer stPagamento;

    @NotNull(message = "Pedido é obrigatório")
    private Long idPedido;

    // Construtores
    public PagamentoDTO() {}

    public PagamentoDTO(String formaPagamento, BigDecimal vlPagamento, Integer stPagamento, Long idPedido) {
        this.formaPagamento = formaPagamento;
        this.vlPagamento = vlPagamento;
        this.stPagamento = stPagamento;
        this.idPedido = idPedido;
    }

    // Getters e Setters
    public Long getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(Long idPagamento) {
        this.idPagamento = idPagamento;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public BigDecimal getVlPagamento() {
        return vlPagamento;
    }

    public void setVlPagamento(BigDecimal vlPagamento) {
        this.vlPagamento = vlPagamento;
    }

    public Integer getStPagamento() {
        return stPagamento;
    }

    public void setStPagamento(Integer stPagamento) {
        this.stPagamento = stPagamento;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }
}