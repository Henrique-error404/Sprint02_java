package com.gestaoestabelecimentos.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "T_ESTABELECIMENTO",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "cnpj", name = "uk_estabelecimento_cnpj")
        }
)
@SequenceGenerator(
        name = "seq_estabelecimento",
        sequenceName = "SQ_T_ESTABELECIMENTO",
        allocationSize = 1
)
public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_estabelecimento")
    @Column(name = "id_estabelecimento")
    private Long idEstabelecimento;

    @NotBlank(message = "Nome do estabelecimento é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    @Column(name = "nm_estabelecimento", length = 100, nullable = false)
    private String nmEstabelecimento;

    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(regexp = "\\d{14}", message = "CNPJ deve conter 14 dígitos")
    @Column(name = "cnpj", length = 14, nullable = false, unique = true)
    private String cnpj;

    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    @Column(name = "endereco_estabelecimento", length = 200)
    private String enderecoEstabelecimento;

    @Size(max = 15, message = "Telefone deve ter no máximo 15 caracteres")
    @Column(name = "tel_estabelecimento", length = 15)
    private String telEstabelecimento;

    @Size(max = 50, message = "Tipo de estabelecimento deve ter no máximo 50 caracteres")
    @Column(name = "tp_estabelecimento", length = 50)
    private String tpEstabelecimento;

    @OneToMany(mappedBy = "estabelecimento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Produto> produtos = new ArrayList<>();

    @OneToMany(mappedBy = "estabelecimento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();

    // Construtores
    public Estabelecimento() {}

    public Estabelecimento(String nmEstabelecimento, String cnpj) {
        this.nmEstabelecimento = nmEstabelecimento;
        this.cnpj = cnpj;
    }

    public Estabelecimento(String nmEstabelecimento, String cnpj, String enderecoEstabelecimento, String telEstabelecimento, String tpEstabelecimento) {
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

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    // Métodos de negócio
    public void adicionarProduto(Produto produto) {
        this.produtos.add(produto);
        produto.setEstabelecimento(this);
    }

    public void removerProduto(Produto produto) {
        this.produtos.remove(produto);
        produto.setEstabelecimento(null);
    }

    public void adicionarPedido(Pedido pedido) {
        this.pedidos.add(pedido);
        pedido.setEstabelecimento(this);
    }

    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estabelecimento that = (Estabelecimento) o;
        return Objects.equals(idEstabelecimento, that.idEstabelecimento) &&
                Objects.equals(cnpj, that.cnpj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEstabelecimento, cnpj);
    }

    // toString
    @Override
    public String toString() {
        return "Estabelecimento{" +
                "idEstabelecimento=" + idEstabelecimento +
                ", nmEstabelecimento='" + nmEstabelecimento + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", enderecoEstabelecimento='" + enderecoEstabelecimento + '\'' +
                ", telEstabelecimento='" + telEstabelecimento + '\'' +
                ", tpEstabelecimento='" + tpEstabelecimento + '\'' +
                '}';
    }
}