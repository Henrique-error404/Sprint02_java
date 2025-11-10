package com.gestaoestabelecimentos.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "T_CUPOM")
@SequenceGenerator(
        name = "seq_cupom",
        sequenceName = "SQ_T_CUPOM",
        allocationSize = 1
)
public class Cupom {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cupom")
    @Column(name = "id_cupom")
    private Long idCupom;

    @NotNull(message = "Valor do cupom é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor do cupom deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Valor do cupom deve ter no máximo 2 casas decimais")
    @Column(name = "vl_cupom", precision = 10, scale = 2, nullable = false)
    private BigDecimal vlCupom;

    @Column(name = "dt_criacao")
    private LocalDateTime dtCriacao;

    @Column(name = "dt_validade")
    private LocalDateTime dtValidade;

    @Column(name = "utilizado")
    private Boolean utilizado = false;

    @NotNull(message = "Usuário é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // Construtores
    public Cupom() {
        this.dtCriacao = LocalDateTime.now();
        this.utilizado = false;
    }

    public Cupom(BigDecimal vlCupom, Usuario usuario) {
        this();
        this.vlCupom = vlCupom;
        this.usuario = usuario;
        // Validade padrão: 30 dias
        this.dtValidade = dtCriacao.plusDays(30);
    }

    public Cupom(BigDecimal vlCupom, LocalDateTime dtValidade, Usuario usuario) {
        this();
        this.vlCupom = vlCupom;
        this.dtValidade = dtValidade;
        this.usuario = usuario;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // Métodos de negócio
    public boolean isValido() {
        return !utilizado && (dtValidade == null || dtValidade.isAfter(LocalDateTime.now()));
    }

    public void utilizar() {
        if (!isValido()) {
            throw new IllegalStateException("Cupom não pode ser utilizado");
        }
        this.utilizado = true;
    }

    public boolean pertenceAoUsuario(Usuario usuario) {
        return this.usuario != null && this.usuario.equals(usuario);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cupom cupom = (Cupom) o;
        return Objects.equals(idCupom, cupom.idCupom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCupom);
    }

    @Override
    public String toString() {
        return "Cupom{" +
                "idCupom=" + idCupom +
                ", vlCupom=" + vlCupom +
                ", dtValidade=" + dtValidade +
                ", utilizado=" + utilizado +
                '}';
    }
}