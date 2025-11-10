package com.gestaoestabelecimentos.repository;

import com.gestaoestabelecimentos.model.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioIdUsuario(Long idUsuario);

    List<Pedido> findByEstabelecimentoIdEstabelecimento(Long idEstabelecimento);

    List<Pedido> findByStPedido(Integer status);

    List<Pedido> findByDtHrPedidoBetween(LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT COALESCE(MAX(p.idPedido), 0) FROM Pedido p")
    Long findMaxId();

    @Query("SELECT p FROM Pedido p WHERE p.usuario.idUsuario = :idUsuario ORDER BY p.dtHrPedido DESC")
    List<Pedido> findPedidosRecentesPorUsuario(@Param("idUsuario") Long idUsuario);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estabelecimento.idEstabelecimento = :idEstabelecimento AND p.stPedido = 1")
    Long countPedidosProntosPorEstabelecimento(@Param("idEstabelecimento") Long idEstabelecimento);
}