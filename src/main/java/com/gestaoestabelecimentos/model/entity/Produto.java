package com.gestaoestabelecimentos.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "T_PRODUTO")
@SequenceGenerator(
        name = "seq_produto",
        sequenceName = "SQ_T_PRODUTO",
        allocationSize = 1
)
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_produto")
    @Column(name = "id_produto")
    private Long idProduto;

    @NotBlank(message = "Nome do produto é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    @Column(name = "nm_produto", length = 100, nullable = false)
    private String nmProduto;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Column(name = "ds_produto", length = 500)
    private String dsProduto;

    @NotNull(message = "Valor do produto é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Valor deve ter no máximo 2 casas decimais")
    @Column(name = "vl_produto", precision = 10, scale = 2, nullable = false)
    private BigDecimal vlProduto;

    @Future(message = "Data de validade deve ser futura")
    @Column(name = "dt_validade")
    private LocalDate dtValidade;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 0, message = "Quantidade não pode ser negativa")
    @Column(name = "qt_produto", nullable = false, precision = 10)
    private Integer qtProduto;

    @NotNull(message = "Estabelecimento é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estabelecimento", nullable = false)
    private Estabelecimento estabelecimento;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemPedido> itensPedido = new ArrayList<>();

    // Construtores
    public Produto() {}

    public Produto(String nmProduto, BigDecimal vlProduto, Integer qtProduto, Estabelecimento estabelecimento) {
        this.nmProduto = nmProduto;
        this.vlProduto = vlProduto;
        this.qtProduto = qtProduto;
        this.estabelecimento = estabelecimento;
    }

    public Produto(String nmProduto, String dsProduto, BigDecimal vlProduto, LocalDate dtValidade, Integer qtProduto, Estabelecimento estabelecimento) {
        this.nmProduto = nmProduto;
        this.dsProduto = dsProduto;
        this.vlProduto = vlProduto;
        this.dtValidade = dtValidade;
        this.qtProduto = qtProduto;
        this.estabelecimento = estabelecimento;
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

    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public List<ItemPedido> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(List<ItemPedido> itensPedido) {
        this.itensPedido = itensPedido;
    }

    // Método de negócio
    public boolean estaValido() {
        return dtValidade == null || dtValidade.isAfter(LocalDate.now());
    }

    public boolean temEstoqueSuficiente(Integer quantidade) {
        return this.qtProduto >= quantidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(idProduto, produto.idProduto) &&
                Objects.equals(nmProduto, produto.nmProduto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduto, nmProduto);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "idProduto=" + idProduto +
                ", nmProduto='" + nmProduto + '\'' +
                ", vlProduto=" + vlProduto +
                ", qtProduto=" + qtProduto +
                '}';
    }
}