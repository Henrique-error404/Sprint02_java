package com.gestaoestabelecimentos.model.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDTO {

    private Long idPedido;

    private LocalDateTime dtHrPedido;

    @NotNull(message = "Valor total é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor total deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Valor total deve ter no máximo 2 casas decimais")
    private BigDecimal vlTotal;

    @NotNull(message = "Status do pedido é obrigatório")
    @Min(value = 0, message = "Status deve ser 0 ou 1")
    @Max(value = 1, message = "Status deve ser 0 ou 1")
    private Integer stPedido;

    @NotNull(message = "Usuário é obrigatório")
    private Long idUsuario;

    @NotNull(message = "Estabelecimento é obrigatório")
    private Long idEstabelecimento;

    private List<ItemPedidoDTO> itens = new ArrayList<>();

    // Construtores
    public PedidoDTO() {}

    public PedidoDTO(BigDecimal vlTotal, Integer stPedido, Long idUsuario, Long idEstabelecimento) {
        this.vlTotal = vlTotal;
        this.stPedido = stPedido;
        this.idUsuario = idUsuario;
        this.idEstabelecimento = idEstabelecimento;
    }

    // Getters e Setters
    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDateTime getDtHrPedido() {
        return dtHrPedido;
    }

    public void setDtHrPedido(LocalDateTime dtHrPedido) {
        this.dtHrPedido = dtHrPedido;
    }

    public BigDecimal getVlTotal() {
        return vlTotal;
    }

    public void setVlTotal(BigDecimal vlTotal) {
        this.vlTotal = vlTotal;
    }

    public Integer getStPedido() {
        return stPedido;
    }

    public void setStPedido(Integer stPedido) {
        this.stPedido = stPedido;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdEstabelecimento() {
        return idEstabelecimento;
    }

    public void setIdEstabelecimento(Long idEstabelecimento) {
        this.idEstabelecimento = idEstabelecimento;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }
}