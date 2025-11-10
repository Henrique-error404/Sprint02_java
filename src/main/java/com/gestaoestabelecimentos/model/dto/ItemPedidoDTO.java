package com.gestaoestabelecimentos.model.dto;

import jakarta.validation.constraints.*;

public class ItemPedidoDTO {

    private Long idItem;

    @NotNull(message = "Quantidade do item é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private Integer qtItem;

    @NotNull(message = "Pedido é obrigatório")
    private Long idPedido;

    @NotNull(message = "Produto é obrigatório")
    private Long idProduto;

    // Construtores
    public ItemPedidoDTO() {}

    public ItemPedidoDTO(Integer qtItem, Long idPedido, Long idProduto) {
        this.qtItem = qtItem;
        this.idPedido = idPedido;
        this.idProduto = idProduto;
    }

    // Getters e Setters
    public Long getIdItem() {
        return idItem;
    }

    public void setIdItem(Long idItem) {
        this.idItem = idItem;
    }

    public Integer getQtItem() {
        return qtItem;
    }

    public void setQtItem(Integer qtItem) {
        this.qtItem = qtItem;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }
}