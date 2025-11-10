package com.gestaoestabelecimentos.repository;

import com.gestaoestabelecimentos.model.entity.Estabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, Long> {

    // Buscar estabelecimento por CNPJ
    Optional<Estabelecimento> findByCnpj(String cnpj);

    // Buscar estabelecimentos por tipo
    List<Estabelecimento> findByTpEstabelecimento(String tipoEstabelecimento);

    // Buscar estabelecimentos por cidade (usando LIKE no endereço)
    @Query("SELECT e FROM Estabelecimento e WHERE e.enderecoEstabelecimento LIKE %:cidade%")
    List<Estabelecimento> findByCidade(@Param("cidade") String cidade);

    // Buscar estabelecimentos por nome (case insensitive)
    List<Estabelecimento> findByNmEstabelecimentoContainingIgnoreCase(String nome);

    // Verificar se CNPJ existe
    boolean existsByCnpj(String cnpj);

    // Verificar se CNPJ existe em outro estabelecimento (para update)
    @Query("SELECT COUNT(e) > 0 FROM Estabelecimento e WHERE e.cnpj = :cnpj AND e.idEstabelecimento != :id")
    boolean existsByCnpjAndIdNot(@Param("cnpj") String cnpj, @Param("id") Long id);

    // Buscar ID máximo para sequences
    @Query("SELECT COALESCE(MAX(e.idEstabelecimento), 0) FROM Estabelecimento e")
    Long findMaxId();

    // Buscar estabelecimentos com produtos em estoque
    @Query("SELECT DISTINCT e FROM Estabelecimento e JOIN e.produtos p WHERE p.qtProduto > 0")
    List<Estabelecimento> findEstabelecimentosComEstoque();

    // Buscar estabelecimentos por tipo e cidade
    @Query("SELECT e FROM Estabelecimento e WHERE e.tpEstabelecimento = :tipo AND e.enderecoEstabelecimento LIKE %:cidade%")
    List<Estabelecimento> findByTipoAndCidade(@Param("tipo") String tipo, @Param("cidade") String cidade);

    // Contar estabelecimentos por tipo
    @Query("SELECT COUNT(e) FROM Estabelecimento e WHERE e.tpEstabelecimento = :tipo")
    Long countByTipoEstabelecimento(@Param("tipo") String tipo);

    // Buscar estabelecimentos ordenados por nome
    List<Estabelecimento> findAllByOrderByNmEstabelecimentoAsc();

    // Buscar estabelecimentos com pedidos recentes (últimos 7 dias)
  //  @Query("SELECT DISTINCT e FROM Estabelecimento e JOIN e.pedidos p WHERE p.dtHrPedido >= CURRENT_DATE - 7")
   // List<Estabelecimento> findEstabelecimentosComPedidosRecentes();
}