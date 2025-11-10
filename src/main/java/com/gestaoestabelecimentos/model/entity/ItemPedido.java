package com.gestaoestabelecimentos.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Objects;
import java.math.BigDecimal;

@Entity
@Table(name = "T_ITEM_PEDIDO")
@SequenceGenerator(
        name = "seq_item_pedido",
        sequenceName = "SQ_T_ITEM_PEDIDO",
        allocationSize = 1
)
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_item_pedido")
    @Column(name = "id_item")
    private Long idItem;

    @NotNull(message = "Quantidade do item é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    @Column(name = "qt_item", nullable = false, precision = 5)
    private Integer qtItem;

    @NotNull(message = "Pedido é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @NotNull(message = "Produto é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    // Construtores
    public ItemPedido() {}

    public ItemPedido(Integer qtItem, Pedido pedido, Produto produto) {
        this.qtItem = qtItem;
        this.pedido = pedido;
        this.produto = produto;
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

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    // Métodos de negócio
    public BigDecimal calcularSubtotal() {
        if (produto != null && produto.getVlProduto() != null && qtItem != null) {
            return produto.getVlProduto().multiply(BigDecimal.valueOf(qtItem));
        }
        return BigDecimal.ZERO;
    }

    public boolean produtoTemEstoqueSuficiente() {
        return produto != null && produto.temEstoqueSuficiente(qtItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedido that = (ItemPedido) o;
        return Objects.equals(idItem, that.idItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idItem);
    }

    @Override
    public String toString() {
        return "ItemPedido{" +
                "idItem=" + idItem +
                ", qtItem=" + qtItem +
                ", produto=" + (produto != null ? produto.getNmProduto() : "null") +
                '}';
    }
}