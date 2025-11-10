package com.gestaoestabelecimentos.repository;

import com.gestaoestabelecimentos.model.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    Optional<Pagamento> findByPedidoIdPedido(Long idPedido);

    List<Pagamento> findByStPagamento(Integer status);

    List<Pagamento> findByFormaPagamento(String formaPagamento);

    @Query("SELECT COALESCE(MAX(p.idPagamento), 0) FROM Pagamento p")
    Long findMaxId();

    @Query("SELECT p FROM Pagamento p WHERE p.pedido.usuario.idUsuario = :idUsuario")
    List<Pagamento> findByUsuarioId(@Param("idUsuario") Long idUsuario);

    @Query("SELECT COUNT(p) FROM Pagamento p WHERE p.stPagamento = 1")
    Long countPagamentosEfetuados();
}