package com.gestaoestabelecimentos.repository;

import com.gestaoestabelecimentos.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByEstabelecimentoIdEstabelecimento(Long idEstabelecimento);

    List<Produto> findByNmProdutoContainingIgnoreCase(String nome);

    List<Produto> findByVlProdutoBetween(BigDecimal precoMin, BigDecimal precoMax);

    List<Produto> findByDtValidadeBefore(LocalDate data);

    List<Produto> findByQtProdutoGreaterThan(Integer quantidade);

    @Query("SELECT p FROM Produto p WHERE p.estabelecimento.idEstabelecimento = :idEstabelecimento AND p.dtValidade > CURRENT_DATE")
    List<Produto> findProdutosValidosPorEstabelecimento(@Param("idEstabelecimento") Long idEstabelecimento);

    @Query("SELECT COALESCE(MAX(p.idProduto), 0) FROM Produto p")
    Long findMaxId();

    @Query("SELECT p FROM Produto p WHERE p.estabelecimento.idEstabelecimento = :idEstabelecimento AND p.qtProduto > 0")
    List<Produto> findProdutosComEstoquePorEstabelecimento(@Param("idEstabelecimento") Long idEstabelecimento);
}