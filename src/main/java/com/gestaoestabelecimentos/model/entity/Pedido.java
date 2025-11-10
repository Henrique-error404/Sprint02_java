package com.gestaoestabelecimentos.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "T_PEDIDO")
@SequenceGenerator(
        name = "seq_pedido",
        sequenceName = "SQ_T_PEDIDO",
        allocationSize = 1
)
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pedido")
    @Column(name = "id_pedido")
    private Long idPedido;

    @NotNull(message = "Data e hora do pedido são obrigatórias")
    @Column(name = "dt_hr_pedido", nullable = false)
    private LocalDateTime dtHrPedido;

    @NotNull(message = "Valor total é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor total deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Valor total deve ter no máximo 2 casas decimais")
    @Column(name = "vl_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal vlTotal;

    @NotNull(message = "Status do pedido é obrigatório")
    @Min(value = 0, message = "Status deve ser 0 ou 1")
    @Max(value = 1, message = "Status deve ser 0 ou 1")
    @Column(name = "st_pedido", nullable = false, precision = 1)
    private Integer stPedido;

    @NotNull(message = "Usuário é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @NotNull(message = "Estabelecimento é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estabelecimento", nullable = false)
    private Estabelecimento estabelecimento;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemPedido> itensPedido = new ArrayList<>();

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Pagamento pagamento;

    // Construtores
    public Pedido() {
        this.dtHrPedido = LocalDateTime.now();
        this.stPedido = 0; // Não pronto
    }

    public Pedido(BigDecimal vlTotal, Usuario usuario, Estabelecimento estabelecimento) {
        this();
        this.vlTotal = vlTotal;
        this.usuario = usuario;
        this.estabelecimento = estabelecimento;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    // Métodos de negócio
    public boolean isPronto() {
        return stPedido != null && stPedido == 1;
    }

    public void marcarComoPronto() {
        this.stPedido = 1;
    }

    public void adicionarItem(ItemPedido item) {
        this.itensPedido.add(item);
        item.setPedido(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(idPedido, pedido.idPedido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPedido);
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", dtHrPedido=" + dtHrPedido +
                ", vlTotal=" + vlTotal +
                ", stPedido=" + stPedido +
                '}';
    }
}