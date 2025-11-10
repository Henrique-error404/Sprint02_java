package com.gestaoestabelecimentos.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "T_PAGAMENTO")
@SequenceGenerator(
        name = "seq_pagamento",
        sequenceName = "SQ_T_PAGAMENTO",
        allocationSize = 1
)
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pagamento")
    @Column(name = "id_pagamento")
    private Long idPagamento;

    @NotBlank(message = "Forma de pagamento é obrigatória")
    @Size(max = 50, message = "Forma de pagamento deve ter no máximo 50 caracteres")
    @Column(name = "forma_pagamento", length = 50, nullable = false)
    private String formaPagamento;

    @NotNull(message = "Valor do pagamento é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor do pagamento deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Valor do pagamento deve ter no máximo 2 casas decimais")
    @Column(name = "vl_pagamento", precision = 10, scale = 2, nullable = false)
    private BigDecimal vlPagamento;

    @NotNull(message = "Status do pagamento é obrigatório")
    @Min(value = 0, message = "Status deve ser 0 ou 1")
    @Max(value = 1, message = "Status deve ser 0 ou 1")
    @Column(name = "st_pagamento", nullable = false, precision = 1)
    private Integer stPagamento;

    @NotNull(message = "Pedido é obrigatório")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false, unique = true)
    private Pedido pedido;

    // Construtores
    public Pagamento() {
        this.stPagamento = 0; // Não pago
    }

    public Pagamento(String formaPagamento, BigDecimal vlPagamento, Pedido pedido) {
        this();
        this.formaPagamento = formaPagamento;
        this.vlPagamento = vlPagamento;
        this.pedido = pedido;
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

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    // Métodos de negócio
    public boolean isPago() {
        return stPagamento != null && stPagamento == 1;
    }

    public void marcarComoPago() {
        this.stPagamento = 1;
    }

    public boolean valorConfereComPedido() {
        return pedido != null && vlPagamento.compareTo(pedido.getVlTotal()) == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pagamento pagamento = (Pagamento) o;
        return Objects.equals(idPagamento, pagamento.idPagamento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPagamento);
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "idPagamento=" + idPagamento +
                ", formaPagamento='" + formaPagamento + '\'' +
                ", vlPagamento=" + vlPagamento +
                ", stPagamento=" + stPagamento +
                '}';
    }
}